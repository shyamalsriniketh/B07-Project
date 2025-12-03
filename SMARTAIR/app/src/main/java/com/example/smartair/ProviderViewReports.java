package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProviderViewReports extends AppCompatActivity {
    FirebaseAuth mAuth;
    String providerId;
    DatabaseReference reference;
    PieChart pieChartZones;
    LineChart lineChartRescue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_view_reports);
        mAuth = FirebaseAuth.getInstance();
        providerId = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference();
        Log.d("hello", "44");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TextView tv = findViewById(R.id.providerReportText);
                tv.setText("");
                for (DataSnapshot code : snapshot.child("providers").child(providerId).child("childrenAndCodes").getChildren()) {
                    Log.d("hello", "33");
                    if (code.getKey().equals(snapshot.child("children").child(code.getValue(String.class)).child("inviteCodeProvider").getValue(String.class))) {
                        //display child's report
                        Log.d("hello", "123");
                        String childId = code.getValue(String.class);
                        loadChildReport(childId);
                        Log.d("hello", "1234");
                    }
                    else {
                        reference.child("providers").child(providerId).child("childrenAndCodes").child(code.getKey()).setValue(null);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        Button button = findViewById(R.id.button10);
        Button button2 = findViewById(R.id.button9);
        button.setOnClickListener(v -> {
            Intent i = new Intent(this, ProviderInviteCodeActivity.class);
            startActivity(i);
        });
        button2.setOnClickListener(v -> {
            Intent j = new Intent(this, SignOut.class);
            startActivity(j);
        });
    }
    private void loadChildReport(String childId){

        DatabaseReference childRef = reference.child("children").child(childId);


        childRef.child("sharingSettings").get().addOnSuccessListener(settingsSnap -> {

            boolean showRescue = getBool(settingsSnap, "rescueFrequency");
            boolean showSymptoms = getBool(settingsSnap, "symptomBurden");
            boolean showZones = getBool(settingsSnap, "zoneDistribution");
            boolean showTriage = getBool(settingsSnap, "triageIncidents");
            boolean showPeakflow = getBool(settingsSnap, "peakFlow");
            boolean showTriggers = getBool(settingsSnap, "triggers");
            boolean showControllerSummary = getBool(settingsSnap, "controllerSummary");
            boolean three_months = getBool(settingsSnap, "threeMonths");
            boolean six_months = getBool(settingsSnap, "sixMonths");
            Log.d("key", " " + showRescue);
            List<String> lines = new ArrayList<>();

            if(showSymptoms){
                reference.child("logs").child(childId).child("dailyCheckin").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot item : snapshot.getChildren()){


                            long date = Long.parseLong(item.getKey());

                            long now = System.currentTimeMillis();
                            long sixMonthsAgo = now - (6L * 30 * 24 * 3600 * 1000);
                            long threeMonthsAgo = now - (3L * 30 * 24 * 60 * 60 * 1000);
                            for(DataSnapshot node : item.getChildren()){
                                String key = node.getKey();
                                if(key.equals("triggers")) continue;

                                String value = String.valueOf(node.getValue());
                                if(three_months && date >= threeMonthsAgo){
                                    appendLine(key + "  ->  " + value);
                                }
                                else if(six_months && date >= sixMonthsAgo){
                                    appendLine(key + "  ->  " + value);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            if(showControllerSummary){
                reference.child("logs").child(childId).child("controller").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot item : snapshot.getChildren()){

                            long date = Long.parseLong(item.getKey());
                            long now = System.currentTimeMillis();
                            long sixMonthsAgo = now - (long)(6L * 30 * 24 * 3600 * 1000);
                            long threeMonthsAgo = now - (long)(3L * 30 * 24 * 60 * 60 * 1000);
                            for(DataSnapshot node : item.getChildren()){
                                String key = node.getKey();
                                Object value = node.getValue();
                                if(three_months && date >= threeMonthsAgo){
                                    appendLine(key + "  ->  " + value);
                                }
                                else if(six_months && date >= sixMonthsAgo){
                                    appendLine(key + "  ->  " + value);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            if(showTriage){
                reference.child("logs").child(childId).child("Logincidents").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot item : snapshot.getChildren()){
                            long date = Long.parseLong(item.getKey());

                            long now = System.currentTimeMillis();
                            long sixMonthsAgo = now - (long)(6L * 30 * 24 * 3600 * 1000);
                            long threeMonthsAgo = now - (long)(3L * 30 * 24 * 60 * 60 * 1000);
                            for(DataSnapshot node : item.getChildren()){
                                if(three_months && date >= threeMonthsAgo){
                                    String key = node.getKey();
                                    Object value = node.getValue();
                                    appendLine(key + "  ->  " + value);
                                }
                                else if(six_months && date >= sixMonthsAgo){
                                    String key = node.getKey();
                                    Object value = node.getValue();
                                    appendLine(key + "  ->  " + value);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            if(showTriggers){
                Log.d("hello", "10");
                reference.child("logs").child(childId).child("dailyCheckin").addListenerForSingleValueEvent(new ValueEventListener(){

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot){
                        Log.d("hello", "9");
                        for(DataSnapshot item : snapshot.getChildren()){

                            long date = Long.parseLong(item.getKey());
                            long now = System.currentTimeMillis();
                            Log.d("hello", "3");
                            long sixMonthsAgo = now - (long)(6L * 30 * 24 * 3600 * 1000);
                            long threeMonthsAgo = now - (long)(3L * 30 * 24 * 60 * 60 * 1000);

                            for(DataSnapshot node : item.getChildren()){
                                if(!(node.getKey().equals("triggers"))) continue;
                                Log.d("hello", "5");
                                for(DataSnapshot trig_child : node.getChildren()){
                                    Object val = trig_child.getValue();
                                    Log.d("hello", "6");
                                    appendLine(val.toString());
                                }
                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // required override
                    }
                });
            }
            if(showRescue){
                reference.child("logs").child(childId).child("rescue").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dates : snapshot.getChildren()){

                            long date = Long.parseLong(dates.getKey());
                            long now = System.currentTimeMillis();
                            long sixMonthsAgo = now - (long)(6L * 30 * 24 * 3600 * 1000);
                            long threeMonthsAgo = now - (long)(3L * 30 * 24 * 60 * 60 * 1000);
                            for(DataSnapshot node : dates.getChildren()){

                                if(three_months && (date >= threeMonthsAgo)) {
                                    String key = node.getKey();
                                    String value = String.valueOf(node.getValue());
                                    appendLine(key + "->" + value);
                                }
                                else if(six_months && (date >= sixMonthsAgo)){
                                    String key = node.getKey();
                                    String value = String.valueOf(node.getValue());
                                    appendLine(key + "->" + value);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        });
    }

    private boolean getBool(DataSnapshot snap, String key) {

        Boolean val = snap.child(key).getValue(Boolean.class);
        Log.d("hello", "100");
        return val != null && val;
    }
    private void appendLine(String text) {
        TextView tv = findViewById(R.id.providerReportText);
        tv.append(text + "\n");
    }

}