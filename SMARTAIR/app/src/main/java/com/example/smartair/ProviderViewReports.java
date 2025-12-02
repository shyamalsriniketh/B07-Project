package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
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
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TextView tv = findViewById(R.id.providerReportText);
                tv.setText("");
                for (DataSnapshot code : snapshot.child("providers").child(providerId).child("childrenAndCodes").getChildren()) {
                    if (code.getKey().equals(snapshot.child("children").child(code.getValue(String.class)).child("inviteCodeProvider").getValue(String.class))) {
                        //display child's report
                        String childId = code.getValue(String.class);
                        loadChildReport(childId);
                    }
                    else {
                        reference.child("providers").child(providerId).child("childrenAndCodes").child(code.getKey()).setValue(null);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        pieChartZones = findViewById(R.id.pieChartZones);
        lineChartRescue = findViewById(R.id.lineChartRescue);
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

            childRef.child("data").get().addOnSuccessListener(dataSnap -> {


                if (showRescue && dataSnap.hasChild("rescueFrequency")) {
                    Long rescue = dataSnap.child("rescueFrequency").getValue(Long.class);
                    showRescueLineChart(rescue);
                }


                if (showSymptoms && dataSnap.hasChild("symptomBurden")) {
                    Long symptoms = dataSnap.child("symptomBurden").getValue(Long.class);
                    appendLine("Symptom burden: " + symptoms);
                }


                if (showZones && dataSnap.hasChild("zoneDistribution")) {
                    Long green = dataSnap.child("zoneDistribution").child("green").getValue(Long.class);
                    Long yellow = dataSnap.child("zoneDistribution").child("yellow").getValue(Long.class);
                    Long red = dataSnap.child("zoneDistribution").child("red").getValue(Long.class);
                    showZonePieChart(green, yellow, red);
                }


                if (showTriage && dataSnap.hasChild("triageIncidents")) {
                    Long triage = dataSnap.child("triageIncidents").getValue(Long.class);
                    appendLine("Triage incidents: " + triage);
                }

                if(showPeakflow && dataSnap.hasChild("peakFlow")){
                    Long peakflow = dataSnap.child("peakFlow").getValue(Long.class);
                    appendLine("Peak Flow: " + peakflow);
                }
            });
        });
    }
    private void showZonePieChart(Long green, Long yellow, Long red) {

        List<PieEntry> entries = new ArrayList<>();

        if (green != null && green > 0) entries.add(new PieEntry(green, "Green"));
        if (yellow != null && yellow > 0) entries.add(new PieEntry(yellow, "Yellow"));
        if (red != null && red > 0) entries.add(new PieEntry(red, "Red"));

        PieDataSet dataSet = new PieDataSet(entries, "Zone Distribution");
        PieData data = new PieData(dataSet);

        pieChartZones.setData(data);
        pieChartZones.getDescription().setEnabled(false);
        pieChartZones.invalidate();
    }
    private void showRescueLineChart(Long rescue) {

        List<Entry> entries = new ArrayList<>();


        entries.add(new Entry(0, rescue != null ? rescue : 0));
        entries.add(new Entry(1, rescue != null ? rescue + 1 : 1));
        entries.add(new Entry(2, rescue != null ? rescue - 1 : 0));

        LineDataSet dataSet = new LineDataSet(entries, "Rescue Frequency");
        LineData data = new LineData(dataSet);

        lineChartRescue.setData(data);
        lineChartRescue.getDescription().setEnabled(false);
        lineChartRescue.invalidate();
    }
    private boolean getBool(DataSnapshot snap, String key) {
        Boolean val = snap.child(key).getValue(Boolean.class);
        return val != null && val;
    }
    private void appendLine(String text) {
        TextView tv = findViewById(R.id.providerReportText);
        tv.append(text + "\n");
    }

}