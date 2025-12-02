package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidplot.xy.XYPlot;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChildDashboardActivity extends AppCompatActivity {
    Button back;
    TextView namebox;
    Child child;
    XYPlot plot;
    Button toggle;
    GraphActivity graph;
    boolean week;
    BottomNavigationView navBar;
    NavBarActivity nav;
    DatabaseReference reference;
    FirebaseUser user;
    TextView zone;
    TextView lastRescueTime;
    TextView weeklyCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_dashboard);
        Intent i = getIntent();
        if (i.hasExtra("PARENT_VIEW")) {
            back = findViewById(R.id.back);
            back.setVisibility(View.VISIBLE);
        }

        nav = new NavBarActivity();
        week = true;
        namebox = findViewById(R.id.textView8);
        plot = findViewById(R.id.plot);
        toggle = findViewById(R.id.button);
        navBar = findViewById(R.id.bottomNavigationView);
        zone = findViewById(R.id.tile_text2);
        lastRescueTime = findViewById(R.id.tile_text4);
        weeklyCount = findViewById(R.id.tile_text6);
        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                child = snapshot.child("children").child(user.getUid()).getValue(Child.class);
                namebox.setText("Welcome "+ child.getName());
                graph = new GraphActivity(plot, child.getId());

                graph.showWeeklyView();
                toggle.setOnClickListener(view -> {
                    if (week) {
                        graph.monthlyView();
                        toggle.setText("Show Weekly");
                        week = false;
                    } else {
                        graph.weeklyView();
                        toggle.setText("Show Monthly");
                        week = true;
                    }
                });

                DataSnapshot latestPEFEntry = null;
                for (DataSnapshot pefEntries : snapshot.child("logs").child(user.getUid()).child("pef").getChildren()) {
                    latestPEFEntry = pefEntries;
                }
                int pb = snapshot.child("children").child(user.getUid()).child("pb").getValue(Integer.class);
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
                    sendRedZoneAlert(snapshot);
                }

                DataSnapshot latestZoneEntry = null;
                for (DataSnapshot zoneEntries : snapshot.child("logs").child(user.getUid()).child("zoneChanges").getChildren()) {
                    latestZoneEntry = zoneEntries;
                }
                if (latestZoneEntry == null || !latestZoneEntry.getValue(String.class).equals(zone.getText().toString())) {
                    reference.child("logs").child(user.getUid()).child("zoneChanges").child(String.valueOf(System.currentTimeMillis())).setValue(zone.getText().toString());
                }

                //set text for last rescue time
                //set text for weekly count

                navBar.setOnItemSelectedListener(item-> {
                    nav.childNav(ChildDashboardActivity.this, item.getItemId(), i);
                    return true;
                });

                back.setOnClickListener(v -> {
                    FirebaseAuth.getInstance().updateCurrentUser(i.getParcelableExtra("PARENT_VIEW"));
                    Intent j = new Intent(ChildDashboardActivity.this, ViewChildActivity.class);
                    startActivity(j);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void sendRedZoneAlert(DataSnapshot snapshot) {
        for (DataSnapshot parent : snapshot.child("parents").getChildren()) {
            for (DataSnapshot child : parent.child("linkedChildren").getChildren()) {
                if (child.getValue(String.class).equals(user.getUid())) {
                    reference.child("alerts").child(parent.getKey()).child("redZone").setValue(true);
                    return;
                }
            }
        }
    }
}