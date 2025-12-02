package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    User u;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_activity);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("parents").hasChild(user.getUid())) {
                    u = new Parent(user.getEmail(), snapshot.child("parents").child(user.getUid()).child("password").toString());
                    u.setOnboarded(snapshot.child("parents").child(user.getUid()).child("onboarded").getValue(Boolean.class));
                }
                else if (snapshot.child("providers").hasChild(user.getUid())) {
                    u = new Provider(user.getEmail(), snapshot.child("providers").child(user.getUid()).child("password").toString());
                    u.setOnboarded(snapshot.child("providers").child(user.getUid()).child("onboarded").getValue(Boolean.class));
                }
                else {
                    u = new Child(snapshot.child("children").child(user.getUid()).child("username").toString(), snapshot.child("children").child(user.getUid()).child("password").toString());
                    u.setOnboarded(snapshot.child("children").child(user.getUid()).child("onboarded").getValue(Boolean.class));
                }
                if (u.getOnboarded()) {
                    goToDashboard();
                }
                ViewPager2 viewPager = findViewById(R.id.viewpager);
                ViewPagerAdapter adapter = new ViewPagerAdapter(OnboardingActivity.this);
                int type = userType();
                adapter.setFragments(getFragmentsForUserType(type));
                viewPager.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


    private List<Fragment> getFragmentsForUserType(int type) {
        switch (type) {
            case 1:
                return Arrays.asList(
                        new ParentOnboardingFragment1(),
                        new ParentOnboardingFragment2(),
                        new ParentOnboardingFragment3()
                );
            case 2:
                return Arrays.asList(
                        new ChildOnboardingFragment1(),
                        new ChildOnboardingFragment2(),
                        new ChildOnboardingFragment3()
                );
            case 3:
                return Arrays.asList(
                        new ProviderOnboardingFragment(),
                        new ProviderOnboardingFragment2(),
                        new ProviderOnboardingFragment3()
                );
            default:
                return Collections.emptyList();
        }
    }
    public int userType() {
        if (u instanceof Parent) {
            return 1;
        }
        if (u instanceof Child) {
            return 2;
        }
        if (u instanceof Provider) {
            return 3;
        }
        return 0;
    }

    public void completeOnboarding() {
        u.setOnboarded(true);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (u instanceof Parent) {
            reference.child("parents").child(fUser.getUid()).child("onboarded").setValue(true);
        }
        else if (u instanceof Provider) {
            reference.child("providers").child(fUser.getUid()).child("onboarded").setValue(true);
        }
        else {
            reference.child("children").child(fUser.getUid()).child("onboarded").setValue(true);
        }
        goToDashboard();
    }

    public void goToDashboard() {
        Intent i;
        if (u instanceof Parent) {
            i = new Intent(this, ParentDashboardActivity.class);
        }
        else if (u instanceof Provider) {
            i = new Intent(this, ProviderViewReports.class);
        }
        else {
            i = new Intent(this, ChildDashboardActivity.class);
        }
        startActivity(i);
    }
}
