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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Provider_Sign_in extends AppCompatActivity {
    EditText emailfield;
    EditText passwordfield;
    Button saveButton;
    FirebaseAuth mAuth;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_provider_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();

        emailfield = findViewById(R.id.ProviderTextEmailAddress);
        passwordfield = findViewById(R.id.ProviderTextTextPassword);
        saveButton = findViewById(R.id.button7);
        saveButton.setOnClickListener(v -> save_button());
    }
    private void save_button(){
        String email = emailfield.getText().toString();
        String password = passwordfield.getText().toString();
        Provider provider = new Provider(email, password);
        String uid = mAuth.getCurrentUser().getUid();
        db.getReference("users").child("providers").child(uid).setValue(provider);
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                FirebaseUser user = mAuth.getCurrentUser();
                Toast.makeText(this, "Account created " + user.getEmail(), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Sign up failed " +
                        task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
    

}