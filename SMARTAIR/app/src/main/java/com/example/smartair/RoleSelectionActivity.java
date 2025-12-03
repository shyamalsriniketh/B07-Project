package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RoleSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.role_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.role), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    public void launchSignupParent(View v){
        Intent i = new Intent(this,Parent_Sign_In.class);
        startActivity(i);
    }
    public void launchSignupProvider(View v){
        Intent i = new Intent(this, Provider_Sign_in.class);
        startActivity(i);
    }
    public void backToLogin(View v){
        Intent i = new Intent(this, LoginView.class);
        startActivity(i);
    }
}