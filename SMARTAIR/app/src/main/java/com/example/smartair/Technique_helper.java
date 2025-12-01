package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class Technique_helper extends AppCompatActivity {
    VideoView videoView;
    Button backButton;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technique_helper);
        videoView = findViewById(R.id.Technique_Helper_Video);
        //TODO: set video logic
        backButton = findViewById(R.id.technique_Helper_Back_Button);
        nextButton = findViewById(R.id.Technique_Helper_Next_Button);
        Intent i = getIntent();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Technique_helper.this, Pre_check.class);
            intent.putExtra("medicineType", getIntent().getStringExtra("medicineType"));
            if (i.hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(Technique_helper.this, Logging_PostCheck.class);
            intent.putExtra("medicineType", getIntent().getStringExtra("medicineType"));
            intent.putExtra("breathRating", getIntent().getStringExtra("breathRating"));
            if (i.hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });
    }
}
