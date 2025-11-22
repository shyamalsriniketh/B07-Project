package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Button button = findViewById(R.id.button3);
        button.setOnClickListener(v -> {
            Intent i = new Intent(this, ManageChildrenActivity.class);
            startActivity(i);
        });
    }
}