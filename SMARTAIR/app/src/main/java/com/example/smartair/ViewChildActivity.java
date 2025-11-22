package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.HashMap;

public class ViewChildActivity extends AppCompatActivity {

    Spinner spinner;
    DatabaseReference reference;
    FirebaseUser user;
    Button save;
    ListView list;
    TextView dataName;
    EditText dataInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_child);
        spinner = findViewById(R.id.child_spinner);
        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        save = findViewById(R.id.save_button);
        list = findViewById(R.id.data_list);
        dataName = findViewById(R.id.data_name);
        dataInput = findViewById(R.id.data_entry);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> children_usernames;
                ArrayAdapter<String> adapter;
                if (snapshot.child("parents").child(user.getUid()).child("linkedChildren").getValue() == null) {
                    Toast.makeText(ViewChildActivity.this, "You don't have any children linked to your account!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ViewChildActivity.this, ManageChildrenActivity.class);
                    startActivity(i);
                }
                children_usernames = new ArrayList<>();
                for (DataSnapshot snapshots : snapshot.child("parents").child(user.getUid()).child("linkedChildren").getChildren()) { //snapshots are each a uid
                    children_usernames.add(snapshot.child("children").child(snapshots.getValue().toString()).child("id").getValue().toString());
                }
                adapter = new ArrayAdapter<>(ViewChildActivity.this, android.R.layout.simple_spinner_item, children_usernames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewChildActivity.this, "Error: couldn't display children", Toast.LENGTH_LONG).show();
            }
        });
        Toast.makeText(this, "Click on an item to edit its value!", Toast.LENGTH_LONG).show();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayChildData(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void displayChildData(String username) {
        ArrayList<String> data = new ArrayList<>();
        reference.child("children").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapshots = null;
                for (DataSnapshot shotChild : snapshot.getChildren()) {
                    if (shotChild.child("id").getValue().toString().equals(username)) {
                        snapshots = shotChild;
                        break;
                    }
                }
                for (String key : ((HashMap<String, Object>) snapshots.getValue()).keySet()) {
                    if (!key.equals("onboarded") && !key.equals("id") && !key.equals("password")) {
                        data.add(key + ": " + snapshots.child(key).getValue());
                    }
                }
                data.add("password: (hidden)");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewChildActivity.this, android.R.layout.simple_list_item_1, data);
                list.setAdapter(adapter);
                list.setVisibility(View.VISIBLE);
                DataSnapshot finalSnapshots = snapshots;
                list.setOnItemClickListener((parent, view, position, id) -> {
                    String keyAndValue = parent.getItemAtPosition(position).toString();
                    String key = keyAndValue.split(":")[0];
                    dataName.setText("Editing " + key);
                    dataInput.setText("");
                    switch (key) {
                        case "password":
                            dataInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            break;
                        case "age":
                            dataInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                            break;
                        case "pb":
                            dataInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                            break;
                        case "highQualitySessionNum":
                            dataInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                            dataName.setText("Editing number of high quality sessions required for a badge");
                            break;
                        case "lowRescueMonthNum":
                            dataInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                            dataName.setText("Editing maximum number of rescue days in a month to earn a badge");
                            break;
                        case "greenActionPlan":
                            dataInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            dataInput.setSingleLine(false);
                            dataInput.setMaxLines(10);
                            dataName.setText("Editing action plan shown to child when zone is green");
                            break;
                        case "yellowActionPlan":
                            dataInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            dataInput.setSingleLine(false);
                            dataInput.setMaxLines(10);
                            dataName.setText("Editing action plan shown to child when zone is yellow");
                            break;
                        case "redActionPlan":
                            dataInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            dataInput.setSingleLine(false);
                            dataInput.setMaxLines(10);
                            dataName.setText("Editing action plan shown to child when zone is red");
                            break;
                        default:
                            dataInput.setInputType(InputType.TYPE_CLASS_TEXT);
                            break;
                    }
                    list.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    dataName.setVisibility(View.VISIBLE);
                    dataInput.setVisibility(View.VISIBLE);
                    save.setVisibility(View.VISIBLE);
                    String finalKey = key;
                    save.setOnClickListener(v -> {
                        if (!dataInput.getText().toString().isEmpty()) {
                            if (finalKey.equals("password")) {
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                mAuth.signInWithEmailAndPassword(username + AddChildActivity.DOMAIN, finalSnapshots.child("password").getValue().toString()).addOnCompleteListener(task -> {
                                    mAuth.getCurrentUser().updatePassword(dataInput.getText().toString()).addOnCompleteListener(task2 -> {
                                        if (!(task2.isSuccessful())) {
                                            Toast.makeText(ViewChildActivity.this, "Couldn't update password: " + task2.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            mAuth.updateCurrentUser(user);
                                            return;
                                        }
                                        reference.child("children").child(finalSnapshots.getKey()).child(finalKey).setValue(dataInput.getText().toString());
                                        mAuth.updateCurrentUser(user);
                                    });
                                });
                            }
                            else if (finalKey.equals("age") || finalKey.equals("pb") || finalKey.equals("highQualitySessionNum") || finalKey.equals("lowRescueMonthNum")) {
                                int input = Integer.parseInt(dataInput.getText().toString());
                                if ((finalKey.equals("age") && input >= 6 && input <= 16)
                                    || (finalKey.equals("pb") && input > 0)
                                    || (finalKey.equals("highQualitySessionNum") && input >= 0)
                                    || (finalKey.equals("lowRescueMonthNum") && input >= 0 && input <= 30)) {
                                    reference.child("children").child(finalSnapshots.getKey()).child(finalKey).setValue(dataInput.getText().toString());
                                }
                                else {
                                    Toast.makeText(ViewChildActivity.this, "Invalid input", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                reference.child("children").child(finalSnapshots.getKey()).child(finalKey).setValue(dataInput.getText().toString());
                            }
                        }
                        dataName.setVisibility(View.INVISIBLE);
                        dataInput.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.INVISIBLE);
                        list.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                    });
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}