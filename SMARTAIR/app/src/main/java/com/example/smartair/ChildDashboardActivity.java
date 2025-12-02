package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.androidplot.xy.XYPlot;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ChildDashboardActivity extends AppCompatActivity {
    Button back;
    TextView namebox;
    Child child;
    XYPlot plot;
    Button toggle;
    GraphActivity graph;
    boolean week= true;
    BottomNavigationView navBar;
    NavBarActivity nav= new NavBarActivity();
    // test data
    Number[] monthlyDate = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
    Number[] monthlyData = {1, 2, 3, 4, 22, 6, 7, 8, 9, 10, 11, 12, 13, 12, 15, 16, 17, 18, 31, 20, 21, 22, 23, 24, 25, 212, 27, 28, 29, 30};
    Number[] weeklyDate = {1, 2, 3, 4, 5, 6, 7};
    Number[] weeklyData = {24, 25, 212, 27, 28, 29, 30};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_dashboard);
        init();
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
        navBar.setOnItemSelectedListener(item-> {
            nav.childNav(ChildDashboardActivity.this, item.getItemId());
            return true;
        });
    }
    public void init(){ //test function
        child= new Child("hego", "b");
        child.name="hego";

    }
}