package com.example.smartair;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    User u;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_activity);
        ViewPager2 viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        int type = userType(u);
        adapter.setFragments(getFragmentsForUserType(type));
        // for testing uncomment and make testusertype =1 for parent 2 for child
        //3 for provider and comment out line 29 and 30 when using
//        int testUserType = 3;
//        adapter.setFragments(getFragmentsForUserType(testUserType));
        viewPager.setAdapter(adapter);



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
    public int userType(User u) {
        if (u.onboarded) {
            return 0;
        }
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
        u.onboarded=true;
    }
}
