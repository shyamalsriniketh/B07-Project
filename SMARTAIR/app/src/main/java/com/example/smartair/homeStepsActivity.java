package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class homeStepsActivity extends AppCompatActivity {








    private CountDownTimer timer;
    private static final long TEN_MINUTES = 10 * 60 * 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_steps);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        startTriageTimer();

        // deciding action plan

        String planText = getIntent().getStringExtra("planText");




        TextView planTextView = findViewById(R.id.planText);
        planTextView.setText(planText);






    }
    private void startTriageTimer() {
        timer = new CountDownTimer(TEN_MINUTES, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Optional: update a visible timer here
                // Or leave it invisible
            }

            @Override
            public void onFinish() {
                // Timer expired: auto-launch new triage
                Intent i = new Intent(homeStepsActivity.this, triageActivity.class);
                i.putExtra("recheck", true);  // mark this as re-triage
                startActivity(i);
                finish();
            }
        }.start();
    }


    public void leaveHomeSteps(View v) {
        // If you have a "back" button in UI
        if (timer != null) timer.cancel();
        finish();
    }


}