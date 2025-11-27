package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
    Button viewAsChild;
    Button shareWithProvider;

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
        viewAsChild = findViewById(R.id.view_as_child);
        shareWithProvider = findViewById(R.id.share_with_provider);
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
                for (DataSnapshot snapshots : snapshot.child("parents").child(user.getUid()).child("linkedChildren").getChildren()) {
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
                DataSnapshot finalSnapshots = snapshots;
                viewAsChild.setOnClickListener(v -> {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(finalSnapshots.getKey() + AddChildActivity.DOMAIN, finalSnapshots.child("password").getValue(String.class));
                    Intent i = new Intent(ViewChildActivity.this, ChildDashboardActivity.class);
                    i.putExtra("PARENT_VIEW", user);
                    startActivity(i);
                });
                shareWithProvider.setOnClickListener(v -> {
                    Intent i = new Intent(ViewChildActivity.this, InvitingProviderActivity.class); //should go to screen where toggles are first
                    i.putExtra("CHILD_UID", finalSnapshots.getKey());
                    startActivity(i);
                });
                for (String key : ((HashMap<String, Object>) snapshots.getValue()).keySet()) {
                    if (!key.equals("onboarded") && !key.equals("id") && !key.equals("password")
                            && !key.equals("inviteCodeProvider") && !key.equals("providerCodeExpiry")) {
                        data.add(key + ": " + snapshots.child(key).getValue());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewChildActivity.this, android.R.layout.simple_list_item_1, data);
                list.setAdapter(adapter);
                list.setVisibility(View.VISIBLE);
                list.setOnItemClickListener((parent, view, position, id) -> {
                    String keyAndValue = parent.getItemAtPosition(position).toString();
                    String key = keyAndValue.split(":")[0];
                    dataName.setText("Editing " + key);
                    dataInput.setText("");
                    switch (key) {
                        case "age":
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
                        case "additionalNotes":
                            dataInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            dataInput.setSingleLine(false);
                            dataInput.setMaxLines(10);
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
                    viewAsChild.setVisibility(View.INVISIBLE);
                    shareWithProvider.setVisibility(View.INVISIBLE);
                    save.setOnClickListener(v -> {
                        if (!dataInput.getText().toString().isEmpty()) {
                            if (key.equals("age") || key.equals("pb") || key.equals("highQualitySessionNum") || key.equals("lowRescueMonthNum")) {
                                int input = Integer.parseInt(dataInput.getText().toString());
                                if ((key.equals("age") && input >= 6 && input <= 16)
                                    || (key.equals("pb") && input > 0)
                                    || (key.equals("highQualitySessionNum") && input >= 0)
                                    || (key.equals("lowRescueMonthNum") && input >= 0 && input <= 30)) {
                                    reference.child("children").child(finalSnapshots.getKey()).child(key).setValue(dataInput.getText().toString());
                                }
                                else {
                                    Toast.makeText(ViewChildActivity.this, "Invalid input", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                reference.child("children").child(finalSnapshots.getKey()).child(key).setValue(dataInput.getText().toString());
                            }
                        }
                        dataName.setVisibility(View.INVISIBLE);
                        dataInput.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.INVISIBLE);
                        list.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                        viewAsChild.setVisibility(View.VISIBLE);
                        shareWithProvider.setVisibility(View.VISIBLE);
                    });
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}