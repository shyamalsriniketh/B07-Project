package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Child_Motivation extends AppCompatActivity {

    Button streaksButton, badgesButton, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_motivation);
        streaksButton = findViewById(R.id.Child_Motivation_Streak_button);
        badgesButton = findViewById(R.id.child_Motivation_Badges_button);
        back = findViewById(R.id.back_button);

        streaksButton.setOnClickListener(v -> {
            Intent intent = new Intent(Child_Motivation.this, Child_Streaks.class);
            if (getIntent().hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) getIntent().getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });

        badgesButton.setOnClickListener(v -> {
            Intent intent = new Intent(Child_Motivation.this, Child_Badges.class);
            if (getIntent().hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) getIntent().getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(Child_Motivation.this, ChildDashboardActivity.class);
            if (getIntent().hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) getIntent().getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });
    }
}
