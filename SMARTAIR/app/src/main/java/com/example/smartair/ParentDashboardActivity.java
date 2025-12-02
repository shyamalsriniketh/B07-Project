package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidplot.xy.XYPlot;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class ParentDashboardActivity extends AppCompatActivity {
    XYPlot plot;
    Button toggle;
    boolean week = true;
    FirebaseAuth mAuth;
    FirebaseUser user;
    GraphActivity graph;
    ArrayList<Child> linkedChildren= new ArrayList<>();
    TextView zone;
    TextView lastRescueTime;
    TextView weeklyCount;
    Button inviteProvider;
    Parent parent;
    Child child1;
    NavBarActivity nav= new NavBarActivity();
    BottomNavigationView navBar;
    Child child2;
    Child child3;
    Child first;
    TextView namebox;
    int len;
    ArrayList<String> childNames= new ArrayList<>();
    AutoCompleteTextView dropdown;
    //test arrays
    Number[] monthlyDate = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
    Number[] monthlyData = {1, 2, 3, 4, 22, 6, 7, 8, 9, 10, 11, 12, 13, 12, 15, 16, 17, 18, 31, 20, 21, 22, 23, 24, 25, 212, 27, 28, 29, 30};
    Number[] weeklyDate = {1, 2, 3, 4, 5, 6, 7};
    Number[] weeklyData = {24, 25, 212, 27, 28, 29, 30};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_dashboard);
//        linkedChildren= parent.getLinkedChildren();
        //comment out init when done testing and un comment linkedChildren= parent.getLinkedChildren();
        init();
        first = linkedChildren.get(0);
        len= linkedChildren.size();
        zone= findViewById(R.id.tile_text2);
        lastRescueTime= findViewById(R.id.tile_text4);
        weeklyCount= findViewById(R.id.tile_text6);
        dropdown= findViewById(R.id.autoCompleteTextView);
        inviteProvider= findViewById(R.id.button2);
        navBar= findViewById(R.id.bottomNavigationView);
        namebox= findViewById(R.id.textView8);
        namebox.setText("Welcome "+ parent.getId());
        dropdown.setFocusable(false);
        dropdown.setClickable(true);
        dropdown.setTextIsSelectable(false);
        dropdown.setInputType(InputType.TYPE_NULL);
        dropdown.setCursorVisible(false);
        dropdown.setKeyListener(null);
        navBar.setOnItemSelectedListener(item-> {
            nav.parentNav(ParentDashboardActivity.this, item.getItemId());
            return true;
        });
        inviteProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParentDashboardActivity.this, InvitingProviderActivity.class);
                startActivity(intent);
            }
        });

        if (len==0){
            //what to show when no chidren yet???
        }
        dropdown.setText(first.getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, childNames);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            Child selectedChild = linkedChildren.get(position);
            Toast.makeText(this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            changeChildView(selectedChild);
        });
        dropdown.setOnClickListener(v -> {
            dropdown.showDropDown();
        });


        plot = findViewById(R.id.plot);
        toggle = findViewById(R.id.button);
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
        changeChildView(first);
    }
    public void changeChildView(Child selectedChild){
        dropdown.setText("");
        dropdown.setHint(selectedChild.getName()+"'s Stats");
        //replace below with real data
        zone.setText(selectedChild.getName());
        lastRescueTime.setText(selectedChild.getName());
        weeklyCount.setText(selectedChild.getName());
//        weeklyData= new Number[]{2, 3, 4, 5, 5, 6, 10};
//        weeklyDate= new Number[]{1, 2, 3, 4, 5, 6, 7};
//        monthlyData= new Number[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 50, 23, 323, 19, 20, 222, 22, 33, 24, 72, 44, 27, 28, 99, 30};;
//        monthlyDate= new Number[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
        graph= new GraphActivity(plot, selectedChild.getId());
        graph.weeklyView();

    }
    public void init(){ //test function
        parent = new Parent("mom", "b");
        child1= new Child("hego", "b");
        child2= new Child("ego", "b");
        child3= new Child("heo", "b");
        child1.name="hego";
        child2.name="ego";
        child3.name="heo";
        linkedChildren.add(child1);
        linkedChildren.add(child2);
        linkedChildren.add(child3);
        childNames.add(child1.getName());
        childNames.add(child2.getName());
        childNames.add(child3.getName());
        parent.setId("mom");
        parent.setLinkedChildren(linkedChildren);
    }
}