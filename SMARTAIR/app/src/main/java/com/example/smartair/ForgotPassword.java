package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPassword extends AppCompatActivity {
    EditText emailInput;
    Button resetButton;
    FirebaseAuth auth;
    EditText passwordInput;
    Button save;
    String email;
    String password;

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
        emailInput = findViewById(R.id.editTextTextEmailAddress);
        resetButton = findViewById(R.id.button3);
        auth = FirebaseAuth.getInstance();
        passwordInput = findViewById(R.id.editTextTextPassword2);
        save = findViewById(R.id.button11);

        resetButton.setOnClickListener(v -> {
           email = emailInput.getText().toString();
           auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   Toast.makeText(this, "Sent reset link to email! Once you reset your password, enter it here to confirm the change.", Toast.LENGTH_SHORT).show();
                   emailInput.setVisibility(View.INVISIBLE);
                   resetButton.setVisibility(View.INVISIBLE);
                   passwordInput.setVisibility(View.VISIBLE);
                   save.setVisibility(View.VISIBLE);

               }
                else {
                   Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
               }
           });
        });

        save.setOnClickListener(v -> {
            password = passwordInput.getText().toString();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
               if (task.isSuccessful()) {
                   updateDatabase();
                   auth.signOut();
                   Intent i = new Intent(this, LoginView.class);
                   startActivity(i);
               }
               else {
                   Toast.makeText(this, "Incorrect password", Toast.LENGTH_LONG).show();
               }
            });

        });
    }

    private void updateDatabase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot parent : snapshot.child("parents").getChildren()) {
                    if (email.equals(parent.child("id").getValue(String.class))) {
                        ref.child("parents").child(parent.getKey()).child("password").setValue(password);
                        return;
                    }
                }
                for (DataSnapshot provider : snapshot.child("providers").getChildren()) {
                    if (email.equals(provider.child("id").getValue(String.class))) {
                        ref.child("providers").child(provider.getKey()).child("password").setValue(password);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}