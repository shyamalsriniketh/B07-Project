package com.example.smartair;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class GraphActivity {
    SimpleXYSeries weeklySeries;
    SimpleXYSeries monthlySeries;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
    LineAndPointFormatter format;
    String userid;
    boolean isDataLoaded=false;
    Runnable onDataLoadedCallback = null;
    XYPlot plot;
    ArrayList<Integer> weeklyData = new ArrayList<Integer>();
    ArrayList<Date> weeklyDate = new ArrayList<Date>();

    ArrayList<Integer> monthlyData = new ArrayList<Integer>();
    ArrayList<Date> monthlyDate =new ArrayList<Date>();

    public GraphActivity(XYPlot plot, String userid) {
        this.plot = plot;
        this.userid=userid;
        //this.weeklySeries= new SimpleXYSeries(Arrays.asList(weeklyData), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "weekly");
        //this.monthlySeries = new SimpleXYSeries(Arrays.asList(monthlyData), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "monthly");
        this.monthlySeries= new SimpleXYSeries("monthly");
        this.weeklySeries= new SimpleXYSeries("weekly");
        this.format= new LineAndPointFormatter(Color.MAGENTA, Color.MAGENTA, null, null);
        getArrays();

    }
    public void weeklyView() {
        if (!isDataLoaded) {
            Log.d("Graph", "Data not loaded yet, waiting...");
            setOnDataLoaded(new Runnable() {
                @Override
                public void run() {
                    showWeeklyView();
                }
            });
            return;
        }
        showWeeklyView();
    }
    public void updateWeeklySeries() {
        weeklySeries.clear();

        if (monthlyDate.size() >= 7) {
            int startIndex = monthlyDate.size() - 7;
            for (int i = 0; i < 7; i++) {
                int sourceIndex = startIndex + i;
                int value = monthlyData.get(sourceIndex);
                weeklySeries.addLast(i, value);
            }
        } else {
            for (int i = 0; i < monthlyDate.size(); i++) {
                weeklySeries.addLast(i, monthlyData.get(i));
            }
        }
    }

    public void updateMonthlySeries() {
        monthlySeries.clear();
        for (int i = 0; i < monthlyDate.size(); i++) {
            monthlySeries.addLast(i, monthlyData.get(i));
        }
    }
    public void getWeeklyArrays(){
        weeklyData.clear();
        weeklyDate.clear();
        if (monthlyDate.size() >= 7) {
            int startIndex = monthlyDate.size() - 7;
            for (int i = startIndex; i < monthlyDate.size(); i++) {
                weeklyData.add(monthlyData.get(i));
                weeklyDate.add(monthlyDate.get(i));
            }
        } else {
            weeklyData.addAll(monthlyData);
            weeklyDate.addAll(monthlyDate);
        }
//        ArrayList<Date> weeklyDate = new ArrayList<Date>();
//        ArrayList<Integer> weeklyData = new ArrayList<Integer>();
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("logs").child(userid).child("pef");
//        long sevenDays = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L);
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot pefEntries : snapshot.getChildren()) {
//                    Long timestamp = Long.parseLong(pefEntries.getKey());
//                    if (timestamp >= sevenDays) {
//                        Integer pefValue = pefEntries.getValue(Integer.class);
//                        weeklyDate.add(new Date(timestamp));
//                        weeklyData.add(pefValue);
//                        removeDupes(weeklyDate,weeklyData);
//                    }
//                }
//            }
//
//    });
//        weeklySeries= new SimpleXYSeries(Arrays.asList(weeklyData), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "weekly");
        }
    public void getArrays(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("logs").child(userid).child("pef");
        long thirtyDays = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot pefEntries : snapshot.getChildren()) {
                    Long timestamp = Long.parseLong(pefEntries.getKey());
                    if (timestamp >= thirtyDays) {
                        Integer pefValue = pefEntries.getValue(Integer.class);
                        monthlyDate.add(new Date(timestamp));
                        monthlyData.add(pefValue);

                    }
                }
                removeDupes(monthlyDate, monthlyData);

                getWeeklyArrays();

                isDataLoaded = true;
                if (onDataLoadedCallback != null) {
                    onDataLoadedCallback.run();
                }

                Log.d("Graph", "Data loaded: " + monthlyDate.size() + " entries");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Graph", "Failed to load data: " + error.getMessage());
            }



        });
    }
    public void setOnDataLoaded(Runnable callback) {
        this.onDataLoadedCallback = callback;
        if (isDataLoaded && callback != null) {
            callback.run();
        }
    }



    public void removeDupes(ArrayList<Date> dates, ArrayList<Integer> values) {
        if (dates.size() <= 1) return;
        for (int i = dates.size() - 1; i > 0; i--) {
            String currentDay = sdf.format(dates.get(i));
            String previousDay = sdf.format(dates.get(i - 1));

            if (currentDay.equals(previousDay)) {
                dates.remove(i - 1);
                values.remove(i - 1);
            }

        }
    }
    public void showWeeklyView(){
        updateWeeklySeries();
        plot.getGraph().setMarginBottom(50);
        plot.setRangeBoundaries(0, 100, BoundaryMode.FIXED);
        plot.setDomainBoundaries(0, 6, BoundaryMode.FIXED);
        plot.setRangeStep(StepMode.SUBDIVIDE, 11);
        plot.getLegend().setVisible(false);
        plot.clear();
        plot.addSeries(weeklySeries, format);
        plot.setDomainStep(StepMode.SUBDIVIDE, 7);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                if (i < 0 || i>= weeklyDate.size()) {
                    return toAppendTo.append("");
                }
                return toAppendTo.append(sdf.format(weeklyDate.get(i)));
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
        plot.redraw();
    }
    public void monthlyView() {
        if (!isDataLoaded) {
            Log.d("Graph", "Data not loaded yet, waiting...");
            setOnDataLoaded(new Runnable() {
                @Override
                public void run() {
                    showMonthlyView();
                }
            });
            return;
        }
        showMonthlyView();
    }
    public void showMonthlyView(){
        updateMonthlySeries();
        plot.getGraph().setMarginBottom(50);
        plot.setDomainBoundaries(0, 29, BoundaryMode.FIXED);
        plot.setRangeBoundaries(0, 100, BoundaryMode.FIXED);
        plot.setDomainStep(StepMode.SUBDIVIDE, 30);
        plot.setRangeStep(StepMode.SUBDIVIDE, 11);
        plot.getLegend().setVisible(false);
        plot.clear();
        plot.addSeries(monthlySeries, format);
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                if (i < 0 || i >= monthlyDate.size()) {
                    return toAppendTo.append("");
                }
                return toAppendTo.append(sdf.format(monthlyDate.get(i)));
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
        plot.redraw();
    }
}
