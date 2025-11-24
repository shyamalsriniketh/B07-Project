package com.example.smartair;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    EditText emailinput;
    Button frgtbtn;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailinput = findViewById(R.id.editTextTextEmailAddress);
        frgtbtn = findViewById(R.id.button3);
        auth = FirebaseAuth.getInstance();

        frgtbtn.setOnClickListener(v -> {
           String email = emailinput.getText().toString();
           auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   Toast.makeText(this, "Reset link to email!", Toast.LENGTH_SHORT).show();

               }
                else {
                   Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
               }
           });
        });
    }
}