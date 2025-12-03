package com.example.smartair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Child_Streaks extends AppCompatActivity {

    Button backButton;
    TextView controllerStreak, techniqueStreak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_streaks);
        backButton = findViewById(R.id.child_Streaks_BackButton);
        controllerStreak = findViewById(R.id.Child_Controller_Streaks_Days);
        techniqueStreak = findViewById(R.id.child_Technique_Streaks_Days);

        int[] streak = new int[1];
        calculateStreak(streak);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Child_Streaks.this, Child_Motivation.class);
            if (getIntent().hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) getIntent().getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });
    }

    public void calculateStreak(int[] streak) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("logs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("controller");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot entries : snapshot.getChildren()) {
                    if (!String.valueOf(entries.getValue(Object.class)).equals("No entry today")) {
                        streak[0]++;
                    }
                    else {
                        streak[0] = 0;
                    }
                }
                controllerStreak.setText(streak[0] + " day(s)");
                techniqueStreak.setText(streak[0] + " day(s)");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
