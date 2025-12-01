package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Logging_PostCheck extends AppCompatActivity {


    Button backButton, nextButton;
    CheckBox betterCheckbox, sameCheckbox, worseCheckbox;
    DatabaseReference reference;
    EditText postCheckBreathRate, puffs, pef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging_post_check);
        Intent intent = getIntent();
        if (intent.getStringExtra("medicineType").equals("Controller")) {
            reference = FirebaseDatabase.getInstance().getReference().child("logs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("controller");
        }
        else {
            reference = FirebaseDatabase.getInstance().getReference().child("logs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("rescue");
        }
        betterCheckbox = findViewById(R.id.Post_Check_Better_Checkbox);
        sameCheckbox = findViewById(R.id.post_Check_Same_Checkbox);
        worseCheckbox = findViewById(R.id.post_Check_Worse_Checkbox);
        postCheckBreathRate = findViewById(R.id.Post_Check_BreathRating);
        puffs = findViewById(R.id.Log_Number_Input);
        backButton = findViewById(R.id.logging_Post_Check_Back_Button);
        nextButton = findViewById(R.id.Logging_Post_Check_Finish_Button);
        pef = findViewById(R.id.Post_check_PEF_entry);
        String status1 = "Better";
        String status2 = "Same";
        String status3 = "Worse";

        backButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(Logging_PostCheck.this, Technique_helper.class);
            intent1.putExtra("medicineType", getIntent().getStringExtra("medicineType"));
            intent1.putExtra("breathRating", getIntent().getStringExtra("breathRating"));
            if (intent.hasExtra("PARENT_VIEW")) {
                intent1.putExtra("PARENT_VIEW", (Parcelable) intent.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent1);
        });

        betterCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                sameCheckbox.setChecked(false);
                worseCheckbox.setChecked(false);
            }
        });

        sameCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                betterCheckbox.setChecked(false);
                worseCheckbox.setChecked(false);
            }
        });

        worseCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                betterCheckbox.setChecked(false);
                sameCheckbox.setChecked(false);
            }
        });

        nextButton.setOnClickListener(v -> {
            String rating = postCheckBreathRate.getText().toString();
            String p = puffs.getText().toString();

            if(!(betterCheckbox.isChecked() || sameCheckbox.isChecked() || worseCheckbox.isChecked())){
                Toast.makeText(Logging_PostCheck.this, "Please select your breathing status", Toast.LENGTH_SHORT).show();
                return;
            }

            if(rating.isEmpty() || p.isEmpty()){
                Toast.makeText(Logging_PostCheck.this, "Please fill your number of puffs and breath rating", Toast.LENGTH_SHORT).show();
                return;
            }

            int numPuffs = Integer.parseInt(p);

            if(numPuffs == 0) {
                Toast.makeText(Logging_PostCheck.this, "Please enter a number greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }

            //TODO: If >=3 rescues in last hour, send alert

            long timestamp = System.currentTimeMillis();
            String status = "";

            if(betterCheckbox.isChecked()){
                status = status1;
            }

            if(sameCheckbox.isChecked()){
                status = status2;
            }

            if(worseCheckbox.isChecked()){
                status = status3;
                //TODO: send alert
            }
            //TODO: manage streaks/badges, adherence stuff

            if (intent.getStringExtra("medicineType").equals("Controller")) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot controllerEntries : snapshot.getChildren()) {
                            if (Long.parseLong(controllerEntries.getKey()) / (1000L * 60 * 60 * 24) == System.currentTimeMillis() / (1000L * 60 * 60 * 24)) {
                                reference.child(controllerEntries.getKey()).removeValue();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }

            reference.child(String.valueOf(timestamp)).child("status").setValue(status);
            reference.child(String.valueOf(timestamp)).child("breathRatingBefore").setValue(getIntent().getStringExtra("breathRating"));
            reference.child(String.valueOf(timestamp)).child("puffs").setValue(numPuffs);
            reference.child(String.valueOf(timestamp)).child("breathRatingAfter").setValue(rating);
            DatabaseReference pefRef = FirebaseDatabase.getInstance().getReference().child("logs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pef");
            if (intent.hasExtra("prePEF") || !pef.getText().toString().isEmpty()) {
                pefRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot pefEntries : snapshot.getChildren()) {
                            if (Long.parseLong(pefEntries.getKey()) / (1000L * 60 * 60 * 24) == System.currentTimeMillis() / (1000L * 60 * 60 * 24) && pefEntries.getValue(String.class).equals("No entry today")) {
                                pefRef.child(pefEntries.getKey()).removeValue();
                                break;
                            }
                        }

                        if (intent.hasExtra("prePEF")) {
                            reference.child(String.valueOf(timestamp)).child("prePEF").setValue(intent.getIntExtra("prePEF", 0));
                            pefRef.child(String.valueOf(intent.getLongExtra("preTimestamp", 0))).setValue(intent.getIntExtra("prePEF", 0));
                        }
                        if (!pef.getText().toString().isEmpty()) {
                            reference.child(String.valueOf(timestamp)).child("postPEF").setValue(Integer.parseInt(pef.getText().toString()));
                            pefRef.child(String.valueOf(timestamp)).setValue(Integer.parseInt(pef.getText().toString()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            Intent intent2 = new Intent(Logging_PostCheck.this, Child_Input.class);
            if (intent.hasExtra("PARENT_VIEW")) {
                intent2.putExtra("PARENT_VIEW", (Parcelable) intent.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent2);
        });
    }
}