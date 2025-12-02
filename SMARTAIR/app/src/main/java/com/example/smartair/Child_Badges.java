package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Child_Badges extends AppCompatActivity {

    Button backButton;
    TextView perfectWeek, techMaster, rescueHero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_badges);
        perfectWeek = findViewById(R.id.child_Badges_Text3);
        techMaster = findViewById(R.id.child_Badges_Text6);
        rescueHero = findViewById(R.id.child_Badges_Text9);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                int streak = 0;
                for (DataSnapshot entries : snapshot.child("logs").child(user.getUid()).child("controller").getChildren()) {
                    if (!String.valueOf(entries.getValue(Object.class)).equals("No entry today")) {
                        streak++;
                    }
                    else {
                        streak = 0;
                    }
                }
                if ((snapshot.child("logs").child(user.getUid()).child("badges").hasChild("controllerWeek")
                        && snapshot.child("logs").child(user.getUid()).child("badges").child("controllerWeek").getValue(Boolean.class))
                        || streak >= 7) {
                    perfectWeek.setText("Unlocked!");
                    reference.child("logs").child(user.getUid()).child("badges").child("controllerWeek").setValue(true);
                }
                else {
                    perfectWeek.setText("Locked");
                }

                int count = 0;
                for (DataSnapshot entries : snapshot.child("logs").child(user.getUid()).child("controller").getChildren()) {
                    if (!String.valueOf(entries.getValue(Object.class)).equals("No entry today")) {
                        count++;
                    }
                }
                if ((snapshot.child("logs").child(user.getUid()).child("badges").hasChild("techniqueMaster")
                        && snapshot.child("logs").child(user.getUid()).child("badges").child("techniqueMaster").getValue(Boolean.class))
                        || count >= snapshot.child("children").child(user.getUid()).child("highQualitySessionNum").getValue(Integer.class)) {
                    techMaster.setText("Unlocked!");
                    reference.child("logs").child(user.getUid()).child("badges").child("techniqueMaster").setValue(true);
                }
                else {
                    techMaster.setText("Locked");
                }

                count = 0;
                for (DataSnapshot entries : snapshot.child("logs").child(user.getUid()).child("rescue").getChildren()) {
                    if (Long.parseLong(entries.getKey()) >= System.currentTimeMillis() - 30 * (1000L * 60 * 60 * 24)) {
                        count++;
                    }
                }
                if ((snapshot.child("logs").child(user.getUid()).child("badges").hasChild("rescueHero")
                        && snapshot.child("logs").child(user.getUid()).child("badges").child("rescueHero").getValue(Boolean.class))
                        || count <= snapshot.child("children").child(user.getUid()).child("lowRescueMonthNum").getValue(Integer.class)) {
                    rescueHero.setText("Unlocked!");
                    reference.child("logs").child(user.getUid()).child("badges").child("rescueHero").setValue(true);
                }
                else {
                    rescueHero.setText("Locked");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        backButton = findViewById(R.id.Child_Badges_BackButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Child_Badges.this, Child_Motivation.class);
            if (getIntent().hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) getIntent().getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });
    }
}
