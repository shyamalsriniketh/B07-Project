package com.example.smartair;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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
    Button edit;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_child);
        spinner = findViewById(R.id.child_spinner);
        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        edit = findViewById(R.id.edit_child);
        list = findViewById(R.id.data_list);
        reference.child("Parents").child(user.getEmail()).child("linkedChildren").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> children_usernames;
                ArrayAdapter<String> adapter;
                if (snapshot.getValue() == null) {
                    Toast.makeText(ViewChildActivity.this, "You don't have any children linked to your account!", Toast.LENGTH_LONG).show();
                    return;
                }
                children_usernames = new ArrayList<>();
                for (DataSnapshot snapshots : snapshot.getChildren()) {
                    children_usernames.add(snapshots.getKey());
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> child = (HashMap<String, String>) parent.getItemAtPosition(position);
                displayChildData(child);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void displayChildData(HashMap<String, String> child) {
        ArrayList<String> data = new ArrayList<>();
        for (String key : child.keySet()) {
            data.add(key + ": " + child.get(key));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        list.setAdapter(adapter);
        edit.setVisibility(View.VISIBLE);
        list.setVisibility(View.VISIBLE);
        //TODO: allow editing of settings
    }
}
//TODO: make all database user keys be UIDs? or when merging parent+provider sign up, change key to be email
//TODO: merge sprint 1 code with new branch, fix any code that doesn't work, refactor login module, and push to main