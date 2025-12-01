package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Pre_check extends AppCompatActivity {

    Button backButton, nextButton;
    EditText breathRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_check);
        backButton = findViewById(R.id.Pre_Check_Back_Button);
        nextButton = findViewById(R.id.Pre_Check_Next_Button);
        breathRating = findViewById(R.id.Pre_check_Breath_Rating);
        Intent i = getIntent();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Pre_check.this, Medication_Selection.class);
            if (i.hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });

        nextButton.setOnClickListener(v -> {
            String rating = breathRating.getText().toString();

            if(rating.isEmpty()){
                Toast.makeText(Pre_check.this, "Please fill in your breath rating", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Pre_check.this, Technique_helper.class);
            intent.putExtra("breathRating", rating);
            intent.putExtra("medicineType", getIntent().getStringExtra("medicineType"));
            if (i.hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });
    }
}
