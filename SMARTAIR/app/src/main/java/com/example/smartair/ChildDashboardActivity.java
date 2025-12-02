package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
    Button input;
    DatabaseReference reference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_dashboard);
        Intent i = getIntent();
        if (i.hasExtra("PARENT_VIEW")) {
            back = findViewById(R.id.back);
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(v -> {
                FirebaseAuth.getInstance().updateCurrentUser(i.getParcelableExtra("PARENT_VIEW"));
                Intent j = new Intent(this, ViewChildActivity.class);
                startActivity(j);
            });
        }
        nav = new NavBarActivity();
        week = true;
        navBar.setOnItemSelectedListener(item-> {
            nav.childNav(ChildDashboardActivity.this, item.getItemId(), getIntent());
            return true;
        });
        namebox= findViewById(R.id.textView8);
        namebox.setText("Welcome "+ child.getName());
        plot = findViewById(R.id.plot);
        toggle = findViewById(R.id.button);
        graph= new GraphActivity(plot,child.getId());
        graph.showWeeklyView();
        navBar= findViewById(R.id.bottomNavigationView);
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
        Button button2 = findViewById(R.id.button13);
        button2.setOnClickListener(v -> {
            Intent l = new Intent(this, Child_Motivation.class);
            if (i.hasExtra("PARENT_VIEW")) {
                l.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(l);
        });
        input = findViewById(R.id.button12);
        input.setOnClickListener(v -> {
            Intent k = new Intent(this, Child_Input.class);
            if (i.hasExtra("PARENT_VIEW")) {
                k.putExtra("PARENT_VIEW", (Parcelable) i.getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(k);
        });

        String[] curZone = new String[1]; //temp variable, delete after proper implementation
        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot latestPEFEntry = null;
                for (DataSnapshot pefEntries : snapshot.child("logs").child(user.getUid()).child("pef").getChildren()) {
                    latestPEFEntry = pefEntries;
                }
                int pb = snapshot.child("children").child(user.getUid()).child("pb").getValue(Integer.class);
                if (String.valueOf(latestPEFEntry.getValue(Object.class)).equals("No entry today") || pb == 0) {
                    curZone[0] = "No zone"; //display "no zone"
                    return;
                }
                int curPEF = latestPEFEntry.getValue(Integer.class);
                double percent = (double) (curPEF) / pb;
                if (percent >= 0.8) {
                    curZone[0] = "Green zone"; //display "green zone"
                }
                else if (percent >= 0.5) {
                    curZone[0] = "Yellow zone"; //display "yellow zone"
                }
                else {
                    curZone[0] = "Red zone"; //display "red zone"
                    sendRedZoneAlert(snapshot);
                }

                DataSnapshot latestZoneEntry = null;
                for (DataSnapshot zoneEntries : snapshot.child("logs").child(user.getUid()).child("zoneChanges").getChildren()) {
                    latestZoneEntry = zoneEntries;
                }
                if (latestZoneEntry == null || !latestZoneEntry.getValue(String.class).equals(curZone[0])) {
                    reference.child("logs").child(user.getUid()).child("zoneChanges").child(String.valueOf(System.currentTimeMillis())).setValue(curZone[0]);
                }
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