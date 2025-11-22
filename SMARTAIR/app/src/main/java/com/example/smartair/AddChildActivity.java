package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddChildActivity extends AppCompatActivity {
    EditText usernameField;
    EditText passwordField;
    Button register;
    Button back;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    final static String DOMAIN = "@smartairchildaccount.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        usernameField = findViewById(R.id.child_username);
        passwordField = findViewById(R.id.child_password);
        register = findViewById(R.id.child_register_button);
        back = findViewById(R.id.add_child_back_button);
        register.setOnClickListener(v -> {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            FirebaseUser curUser = mAuth.getCurrentUser();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(username + DOMAIN, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Child child = new Child(username, password);
                    reference.child("children").child(mAuth.getCurrentUser().getUid()).setValue(child);
                    reference.child("parents").child(curUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<String> childList;
                            if (!snapshot.hasChild("linkedChildren")) {
                                childList = new ArrayList<>();
                            }
                            else {
                                childList = (ArrayList<String>) snapshot.child("linkedChildren").getValue();
                            }
                            childList.add(mAuth.getCurrentUser().getUid());
                            reference.child("parents").child(curUser.getUid()).child("linkedChildren").setValue(childList);
                            Toast.makeText(AddChildActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                            mAuth.updateCurrentUser(curUser);
                            back.performClick();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AddChildActivity.this, "Error: couldn't link child account to parent.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    Toast.makeText(this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
        back.setOnClickListener(v -> {
            Intent i = new Intent(this, ManageChildrenActivity.class);
            startActivity(i);
        });
    }

}