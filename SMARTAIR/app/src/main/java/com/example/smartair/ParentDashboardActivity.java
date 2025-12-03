package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.androidplot.xy.XYPlot;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class ParentDashboardActivity extends AppCompatActivity {
    XYPlot plot;
    Button toggle;
    boolean week;
    //GraphActivity graph;
    TextView zone;
    TextView lastRescueTime;
    TextView weeklyCount;
    Parent par;
    NavBarActivity nav;
    BottomNavigationView navBar;
    TextView namebox;
    int len;
    ArrayList<String> childNames;
    AutoCompleteTextView dropdown;
    DatabaseReference reference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_dashboard);
        displayAlerts();
        nav = new NavBarActivity();
        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        zone = findViewById(R.id.tile_text2);
        lastRescueTime = findViewById(R.id.tile_text4);
        weeklyCount = findViewById(R.id.tile_text6);
        dropdown = findViewById(R.id.autoCompleteTextView);
        navBar = findViewById(R.id.bottomNavigationView);
        namebox = findViewById(R.id.textView8);
        plot = findViewById(R.id.plot);
        toggle = findViewById(R.id.button);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                par = snapshot.child("parents").child(user.getUid()).getValue(Parent.class);
                week = true;
                childNames = new ArrayList<>();
                for (String childUid : par.getLinkedChildren()) {
                    childNames.add(snapshot.child("children").child(childUid).child("id").getValue(String.class));
                }
                len = childNames.size();
                namebox.setText("Welcome " + par.getId());

                if (len == 0) {
                    Toast.makeText(ParentDashboardActivity.this, "No children linked yet!", Toast.LENGTH_SHORT).show();
                    return;
                }

                dropdown.setFocusable(false);
                dropdown.setClickable(true);
                dropdown.setTextIsSelectable(false);
                dropdown.setInputType(InputType.TYPE_NULL);
                dropdown.setCursorVisible(false);
                dropdown.setKeyListener(null);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ParentDashboardActivity.this, android.R.layout.simple_dropdown_item_1line, childNames);
                dropdown.setAdapter(adapter);
                dropdown.setOnClickListener(v -> dropdown.showDropDown());

                /*toggle.setOnClickListener(view -> {
                    if (dropdown.getText().toString().equals("Select a child")) {
                        return;
                    }
                    if (week) {
                        graph.monthlyView();
                        toggle.setText("Show Weekly");
                        week = false;
                    } else {
                        graph.weeklyView();
                        toggle.setText("Show Monthly");
                        week = true;
                    }
                });*/

                dropdown.setOnItemClickListener((parent, view, position, id) -> {
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    String selectedChild = par.getLinkedChildren().get(position);
                    Toast.makeText(ParentDashboardActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                    changeChildView(selectedChild, selectedItem);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        navBar.setOnItemSelectedListener(item-> {
            nav.parentNav(ParentDashboardActivity.this, item.getTitle().toString());
            return true;
        });
    }
    public void changeChildView(String childUid, String childName){
        dropdown.setText("");
        dropdown.setHint(childName + "'s data");
        /*graph = new GraphActivity(plot, childUid);
        if (week) {
            graph.weeklyView();
        } else {
            graph.monthlyView();
        }*/
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot lastRescue = null;
                for (DataSnapshot rescue : snapshot.child("logs").child(childUid).child("rescue").getChildren()) {
                    lastRescue = rescue;
                }
                if (lastRescue == null) {
                    lastRescueTime.setText("No rescues yet");
                }
                else {
                    lastRescueTime.setText(((System.currentTimeMillis() - Long.parseLong(lastRescue.getKey())) / (1000.0 * 60.0 * 60.0)) + " hours ago");
                }

                int count = 0;
                for (DataSnapshot rescue : snapshot.child("logs").child(childUid).child("rescue").getChildren()) {
                    if (Long.parseLong(rescue.getKey()) > System.currentTimeMillis() - 7 * (1000L * 60 * 60 * 24)) {
                        count++;
                    }
                }
                weeklyCount.setText(String.valueOf(count));

                DataSnapshot latestPEFEntry = null;
                for (DataSnapshot pefEntries : snapshot.child("logs").child(childUid).child("pef").getChildren()) {
                    latestPEFEntry = pefEntries;
                }
                int pb = snapshot.child("children").child(childUid).child("pb").getValue(Integer.class);
                if (String.valueOf(latestPEFEntry.getValue(Object.class)).equals("No entry today") || pb == 0) {
                    zone.setText("No zone");
                    return;
                }
                int curPEF = latestPEFEntry.getValue(Integer.class);
                double percent = (double) (curPEF) / pb;
                if (percent >= 0.8) {
                    zone.setText("Green zone");
                }
                else if (percent >= 0.5) {
                    zone.setText("Yellow zone");
                }
                else {
                    zone.setText("Red zone");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void displayAlerts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("alerts").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot message : snapshot.child("triageAlerts").getChildren()) {
                    Toast.makeText(ParentDashboardActivity.this, "ALERT!: " + message.getValue(String.class), Toast.LENGTH_LONG).show();
                }
                reference.child("triageAlerts").removeValue();
                if (snapshot.hasChild("rapidRescue") && snapshot.child("rapidRescue").getValue(Boolean.class)) {
                    Toast.makeText(ParentDashboardActivity.this, "ALERT!: Your child has taken 3 or more rescue doses within 3 hours", Toast.LENGTH_LONG).show();
                    reference.child("rapidRescue").setValue(false);
                }
                if (snapshot.hasChild("redZone") && snapshot.child("redZone").getValue(Boolean.class)) {
                    Toast.makeText(ParentDashboardActivity.this, "ALERT!: Your child entered the red zone", Toast.LENGTH_LONG).show();
                    reference.child("redZone").setValue(false);
                }
                if (snapshot.hasChild("worseAfterDose") && snapshot.child("worseAfterDose").getValue(Boolean.class)) {
                    Toast.makeText(ParentDashboardActivity.this, "ALERT!: Your child is feeling worse after taking a dose of medicine", Toast.LENGTH_LONG).show();
                    reference.child("worseAfterDose").setValue(false);
                }
                if (snapshot.hasChild("inventoryExpired") && snapshot.child("inventoryExpired").getValue(Boolean.class)) {
                    Toast.makeText(ParentDashboardActivity.this, "ALERT!: Your child's medicine has expired", Toast.LENGTH_LONG).show();
                    reference.child("inventoryExpired").setValue(false);
                }
                if (snapshot.hasChild("inventoryLow") && snapshot.child("inventoryLow").getValue(Boolean.class)) {
                    Toast.makeText(ParentDashboardActivity.this, "ALERT!: Your child's medicine canister is low, or has been marked by your child as low", Toast.LENGTH_LONG).show();
                    reference.child("inventoryLow").setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}