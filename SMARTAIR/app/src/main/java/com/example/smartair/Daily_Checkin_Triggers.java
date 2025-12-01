package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Daily_Checkin_Triggers extends AppCompatActivity {

    Button backButton, nextButton;
    CheckBox exerciseCheckbox, coldAirCheckbox, dustPetsCheckbox, smokeCheckbox, illnessCheckbox, perfumesCheckbox, noneCheckbox;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_checkin_triggers);
        reference = FirebaseDatabase.getInstance().getReference().child("logs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("dailyCheckin");
        exerciseCheckbox = findViewById(R.id.Daily_Checkin_Trigger_Exercise_Checkbox);
        coldAirCheckbox = findViewById(R.id.daily_Checkin_Trigger_Cold_Air_Checkbox);
        dustPetsCheckbox = findViewById(R.id.daily_Checkin_Trigger_DustPets_Checkbox);
        smokeCheckbox = findViewById(R.id.daily_Checkin_Trigger_Smoke_Checkbox);
        illnessCheckbox = findViewById(R.id.daily_Checkin_Trigger_Illness_Checkbox);
        perfumesCheckbox = findViewById(R.id.daily_Checkin_Trigger_Perfumes_Checkbox);
        noneCheckbox = findViewById(R.id.daily_Checkin_Trigger_None_Checkbox);
        backButton = findViewById(R.id.daily_Checkin_Triggers_Back_Button);
        nextButton = findViewById(R.id.Daily_Checkin_Triggers_Finish_Button);
        String t1 = "Exercise";
        String t2 = "Cold Air";
        String t3 = "Dust/Pets";
        String t4 = "Smoke";
        String t5 = "Illness";
        String t6 = "Perfumes";
        String t7 = "None";
        Intent i = getIntent();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Daily_Checkin_Triggers.this, Daily_Checkin.class);
            if (i.hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });

        noneCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                exerciseCheckbox.setChecked(false);
                coldAirCheckbox.setChecked(false);
                dustPetsCheckbox.setChecked(false);
                smokeCheckbox.setChecked(false);
                illnessCheckbox.setChecked(false);
                perfumesCheckbox.setChecked(false);
            }
        });

        exerciseCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                noneCheckbox.setChecked(false);
            }
        });

        coldAirCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                noneCheckbox.setChecked(false);
            }
        });

        dustPetsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                noneCheckbox.setChecked(false);
            }
        });

        smokeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                noneCheckbox.setChecked(false);
            }
        });

        illnessCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                noneCheckbox.setChecked(false);
            }
        });

        perfumesCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                noneCheckbox.setChecked(false);
            }
        });

        nextButton.setOnClickListener(v -> {
            if (!(exerciseCheckbox.isChecked() || coldAirCheckbox.isChecked() || dustPetsCheckbox.isChecked() || smokeCheckbox.isChecked() || illnessCheckbox.isChecked() || perfumesCheckbox.isChecked() || noneCheckbox.isChecked())) {
                Toast.makeText(Daily_Checkin_Triggers.this, "Please select any triggers you had", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<String> triggers = new ArrayList<>();
            long timestamp = System.currentTimeMillis();
            String nightWalking = i.getStringExtra("nightWalking");
            String limits = i.getStringExtra("limits");
            String coughing = i.getStringExtra("coughingAndWheezing");

            if(exerciseCheckbox.isChecked()){
                triggers.add(t1);
            }

            if(coldAirCheckbox.isChecked()){
                triggers.add(t2);
            }

            if(dustPetsCheckbox.isChecked()){
                triggers.add(t3);
            }

            if(smokeCheckbox.isChecked()){
                triggers.add(t4);
            }

            if(illnessCheckbox.isChecked()){
                triggers.add(t5);
            }

            if(perfumesCheckbox.isChecked()){
                triggers.add(t6);
            }

            if(noneCheckbox.isChecked()){
                triggers.add(t7);
            }

            reference.child(String.valueOf(timestamp)).child("nightWalking").setValue(nightWalking);
            reference.child(String.valueOf(timestamp)).child("limits").setValue(limits);
            reference.child(String.valueOf(timestamp)).child("coughingAndWheezing").setValue(coughing);
            reference.child(String.valueOf(timestamp)).child("triggers").setValue(triggers);

            Intent intent = new Intent(Daily_Checkin_Triggers.this, Child_Input.class);
            if (i.hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
                reference.child(String.valueOf(timestamp)).child("markedBy").setValue("parent");
            }
            else {
                reference.child(String.valueOf(timestamp)).child("markedBy").setValue("child");
            }
            startActivity(intent);
        });
    }
}
