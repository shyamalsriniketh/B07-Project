package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ManageChildrenActivity extends AppCompatActivity {
    Button add_child;
    Button manage_child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_children);
        add_child = findViewById(R.id.addChildButton);
        manage_child = findViewById(R.id.manageChildButton);
        add_child.setOnClickListener(v -> {
            Intent i = new Intent(this, AddChildActivity.class);
            startActivity(i);
        });
        manage_child.setOnClickListener(v -> {
            Intent i = new Intent(this, ViewChildActivity.class);
            startActivity(i);
        });
    }
}