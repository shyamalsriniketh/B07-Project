package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Child_Input extends AppCompatActivity {

    Button medicationButton;
    Button checkinButton;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_input);
        Intent i = getIntent();

        medicationButton = findViewById(R.id.Log_Medicine_Button);
        medicationButton.setOnClickListener(v -> {
            Intent intent = new Intent(Child_Input.this, Medication_Selection.class);
            if (i.hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });

        checkinButton = findViewById(R.id.daily_Check_in_Button);
        checkinButton.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference().child("logs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("dailyCheckin").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshots : snapshot.getChildren()) {
                        if (Long.parseLong(snapshots.getKey()) / (1000L * 60 * 60 * 24) == System.currentTimeMillis() / (1000L * 60 * 60 * 24)) {
                            Toast.makeText(Child_Input.this, "You have already checked in today!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Intent intent = new Intent(Child_Input.this, Daily_Checkin.class);
                    if (i.hasExtra("PARENT_VIEW")) {
                        intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
                    }
                    startActivity(intent);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(Child_Input.this, ChildDashboardActivity.class);
            if (i.hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });
    }
}
