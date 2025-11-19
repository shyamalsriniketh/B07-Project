package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
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
    EditText nameField;
    EditText dobField;
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
        nameField = findViewById(R.id.child_name);
        dobField = findViewById(R.id.child_dob);
        register = findViewById(R.id.child_register_button);
        back = findViewById(R.id.add_child_back_button);
        Toast.makeText(this, "Once you create your child's account, you'll have to log in again. Don't worry, you won't lose any data!", Toast.LENGTH_SHORT).show();
        register.setOnClickListener(v -> {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            String name = nameField.getText().toString();
            String dob = dobField.getText().toString();
            FirebaseUser curUser = mAuth.getCurrentUser();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(username + DOMAIN, password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    Child child = new Child(username, password);
                    if (!name.isEmpty()) {
                        child.setName(name);
                    }
                    if (!dob.isEmpty()) {
                        child.setDob(dob);
                    }
                    reference.child("Children").child(username).setValue(child);
                    reference.child("Parents").child(curUser.getEmail()).child("linkedChildren").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<Child> childList;
                            if (snapshot.getValue() == null) {
                                childList = new ArrayList<>();
                            }
                            else {
                                childList = (ArrayList<Child>) snapshot.getValue();
                            }
                            childList.add(child);
                            reference.child("Parents").child(curUser.getEmail()).child("linkedChildren").setValue(childList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AddChildActivity.this, "Error: couldn't link child account to parent.", Toast.LENGTH_LONG).show();
                        }
                    });
                    Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    Intent i = new Intent(this, Login.class);
                    startActivity(i);
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