package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Daily_Checkin extends AppCompatActivity {

    Button backButton, nextButton;
    CheckBox noWalkingCheckbox, walking1Checkbox, walking2Checkbox, walking3Checkbox, noLimitsCheckbox, someCheckbox, manyCheckbox, noneCheckbox, mildCheckbox, moderateCheckbox,severeCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_checkin);
        noWalkingCheckbox = findViewById(R.id.Night_Waking_No_Checkbox);
        walking1Checkbox = findViewById(R.id.night_Waking_1_Checkbox);
        walking2Checkbox = findViewById(R.id.night_Waking_2_Checkbox);
        walking3Checkbox = findViewById(R.id.night_Waking_3_Checkbox);
        noLimitsCheckbox = findViewById(R.id.activityLimits_No_Checkbox);
        someCheckbox = findViewById(R.id.activityLimits_Some_Checkbox);
        manyCheckbox = findViewById(R.id.activityLimits_Many_Checkbox);
        noneCheckbox = findViewById(R.id.Cough_No_Checkbox);
        mildCheckbox = findViewById(R.id.cough_Mild_Checkbox);
        moderateCheckbox = findViewById(R.id.cough_Moderate_Checkbox2);
        severeCheckbox = findViewById(R.id.cough_Severe_Checkbox);
        backButton = findViewById(R.id.daily_Checkin_Back1_Button);
        nextButton = findViewById(R.id.Daily_Checkin_Next1_Button);
        String walking0 = "No walking";
        String walking1 = "1 time";
        String walking2 = "2 times";
        String walking3 = "3+ times";
        String l1 = "No limits";
        String l2 = "Some limits";
        String l3 = "Many limits";
        String c1 = "None";
        String c2 = "Mild";
        String c3 = "Moderate";
        String c4 = "Severe";
        Intent i = getIntent();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Daily_Checkin.this, Child_Input.class);
            if (i.hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });

        noWalkingCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                walking1Checkbox.setChecked(false);
                walking2Checkbox.setChecked(false);
                walking3Checkbox.setChecked(false);
            }
        });

        walking1Checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                noWalkingCheckbox.setChecked(false);
                walking2Checkbox.setChecked(false);
                walking3Checkbox.setChecked(false);
            }
        });

        walking2Checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                noWalkingCheckbox.setChecked(false);
                walking1Checkbox.setChecked(false);
                walking3Checkbox.setChecked(false);
            }
        });

        walking3Checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                noWalkingCheckbox.setChecked(false);
                walking1Checkbox.setChecked(false);
                walking2Checkbox.setChecked(false);
            }
        });

        noLimitsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                someCheckbox.setChecked(false);
                manyCheckbox.setChecked(false);
            }
        });

        someCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                noLimitsCheckbox.setChecked(false);
                manyCheckbox.setChecked(false);
            }
        });

        manyCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                noLimitsCheckbox.setChecked(false);
                someCheckbox.setChecked(false);
            }
        });

        noneCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                mildCheckbox.setChecked(false);
                moderateCheckbox.setChecked(false);
                severeCheckbox.setChecked(false);
            }
        });

        mildCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                noneCheckbox.setChecked(false);
                moderateCheckbox.setChecked(false);
                severeCheckbox.setChecked(false);
            }
        });

        moderateCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                noneCheckbox.setChecked(false);
                mildCheckbox.setChecked(false);
                severeCheckbox.setChecked(false);
            }
        });

        severeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                noneCheckbox.setChecked(false);
                mildCheckbox.setChecked(false);
                moderateCheckbox.setChecked(false);
            }
        });

        nextButton.setOnClickListener(v -> {
            if(!(noWalkingCheckbox.isChecked() || walking1Checkbox.isChecked() || walking2Checkbox.isChecked() || walking3Checkbox.isChecked())){
                Toast.makeText(Daily_Checkin.this, "Please select your night walking status", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!(noLimitsCheckbox.isChecked() || someCheckbox.isChecked() || manyCheckbox.isChecked())){
                Toast.makeText(Daily_Checkin.this, "Please select your activity limitations", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!(noneCheckbox.isChecked() || mildCheckbox.isChecked() || moderateCheckbox.isChecked() || severeCheckbox.isChecked())){
                Toast.makeText(Daily_Checkin.this, "Please select your cough/wheeze status", Toast.LENGTH_SHORT).show();
                return;
            }

            String walking = "";
            String limits = "";
            String cough = "";

            if (noWalkingCheckbox.isChecked()){
                walking = walking0;
            }

            if (walking1Checkbox.isChecked()){
                walking = walking1;
            }

            if (walking2Checkbox.isChecked()){
                walking = walking2;
            }

            if (walking3Checkbox.isChecked()){
                walking = walking3;
            }

            if (noLimitsCheckbox.isChecked()){
                limits = l1;
            }

            if (someCheckbox.isChecked()){
                limits = l2;
            }

            if (manyCheckbox.isChecked()){
                limits = l3;
            }

            if (noneCheckbox.isChecked()){
                cough = c1;
            }

            if (mildCheckbox.isChecked()){
                cough = c2;
            }

            if (moderateCheckbox.isChecked()){
                cough = c3;
            }

            if (severeCheckbox.isChecked()){
                cough = c4;
            }

            Intent intent = new Intent(Daily_Checkin.this, Daily_Checkin_Triggers.class);
            intent.putExtra("nightWalking", walking);
            intent.putExtra("limits", limits);
            intent.putExtra("coughingAndWheezing", cough);
            if (i.hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });
    }
}
