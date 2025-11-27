package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class ChildDashboardActivity extends AppCompatActivity {
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_dashboard);
        Intent i = getIntent();
        if (i.hasExtra("PARENT_VIEW")) {
            back = findViewById(R.id.back);
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(v -> {
                FirebaseAuth.getInstance().updateCurrentUser(i.getParcelableExtra("PARENT_VIEW"));
                Intent j = new Intent(this, ViewChildActivity.class);
                startActivity(j);
            });
        }
        Button button = findViewById(R.id.button8);
        button.setOnClickListener(v -> {
            Intent j = new Intent(this, SignOut.class);
            startActivity(j);
        });
    }
}