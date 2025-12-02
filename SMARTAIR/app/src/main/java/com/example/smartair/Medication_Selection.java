package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Medication_Selection extends AppCompatActivity {

    Button backButton, nextButton;
    CheckBox rescueCheckbox, controllerCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_selection);
        rescueCheckbox = findViewById(R.id.Rescue_Medication_CheckBox);
        controllerCheckbox = findViewById(R.id.Controller_Medication_Checkbox);
        backButton = findViewById(R.id.medication_Selection_Back_Button);
        nextButton = findViewById(R.id.Medication_Selection_Next_Button);
        String m1 = "Rescue";
        String m2 = "Controller";
        Intent i = getIntent();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Medication_Selection.this, Child_Input.class);
            if (i.hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });

        rescueCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                controllerCheckbox.setChecked(false);
            }
        });

        controllerCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                rescueCheckbox.setChecked(false);
            }
        });

        nextButton.setOnClickListener(v -> {
            if(!(rescueCheckbox.isChecked() || controllerCheckbox.isChecked())){
                Toast.makeText(Medication_Selection.this, "Please select the medicine you want to use", Toast.LENGTH_SHORT).show();
                return;
            }

            if(rescueCheckbox.isChecked()){
                Intent intent = new Intent(Medication_Selection.this, Pre_check.class);
                intent.putExtra("medicineType", m1);
                if (i.hasExtra("PARENT_VIEW")) {
                    intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
                }
                startActivity(intent);
                return;
            }
            FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.child("children").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("controllerToday").getValue(Boolean.class)) {
                        Toast.makeText(Medication_Selection.this, "You are not scheduled to take a controller dose today.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (DataSnapshot snapshots : snapshot.child("logs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("controller").getChildren()) {
                        if (Long.parseLong(snapshots.getKey()) / (1000L * 60 * 60 * 24) == System.currentTimeMillis() / (1000L * 60 * 60 * 24) && !String.valueOf(snapshots.getValue(Object.class)).equals("No entry today")) {
                            Toast.makeText(Medication_Selection.this, "You have already taken today's controller dose!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Intent intent = new Intent(Medication_Selection.this, Pre_check.class);
                    intent.putExtra("medicineType", m2);
                    if (i.hasExtra("PARENT_VIEW")) {
                        intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
                    }
                    startActivity(intent);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        });
    }
}