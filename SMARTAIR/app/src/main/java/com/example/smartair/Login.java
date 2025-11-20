package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText editTextEmailUsername, editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        editTextEmailUsername = findViewById(R.id.enterEmailOrUsername);
        editTextPassword = findViewById(R.id.enterPassword);
        buttonLogin = findViewById(R.id.button_Login);
        textView = findViewById(R.id.registerNow);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RoleSelectionActivity.class);
            startActivity(intent);
            finish();
        });

        buttonLogin.setOnClickListener(view -> {
            String emailOrUsername, password;
            emailOrUsername = String.valueOf(editTextEmailUsername.getText());
            password = String.valueOf(editTextPassword.getText());

            if(TextUtils.isEmpty(emailOrUsername)){
                Toast.makeText(Login.this, "Enter email or username", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
            }

            mAuth.signInWithEmailAndPassword(emailOrUsername, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), OnboardingActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }
}