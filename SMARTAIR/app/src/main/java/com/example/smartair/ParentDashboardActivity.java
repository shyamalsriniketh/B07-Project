package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ParentDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);
        //TODO: check for alerts
        Button button = findViewById(R.id.button3);
        button.setOnClickListener(v -> {
            Intent i = new Intent(this, ManageChildrenActivity.class);
            startActivity(i);
        });
        Button button2 = findViewById(R.id.button6);
        button2.setOnClickListener(v -> {
            Intent i = new Intent(this, SignOut.class);
            startActivity(i);
        });
    }
}