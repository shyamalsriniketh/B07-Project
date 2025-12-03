package com.example.smartair;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.firebase.database.*;

public class PEFFetcher {

    // STEP 1: Create callback interface
    public interface GraphDataCallback {
        void onArraysReady(Number[] weeklyData, Number[] weeklyDates,
                           Number[] monthlyData, Number[] monthlyDates);
        void onError(String errorMessage);
    }

    // STEP 2: Main fetching method
    public static void fetchDataForGraph(String userId, GraphDataCallback callback) {

        // STEP 3: Get Firebase reference to your PEF data
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("logs")
                .child(userId)
                .child("pef");

        // STEP 4: MUST use this listener - Firebase requires it!
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // STEP 5: This runs WHEN data arrives from Firebase

                // STEP 6: Create temporary lists
                List<Number> weeklyValues = new ArrayList<>();
                List<Number> monthlyValues = new ArrayList<>();

                // STEP 7: Loop through Firebase data
                for (DataSnapshot entry : snapshot.getChildren()) {
                    try {
                        // Get timestamp and value
                        long timestamp = Long.parseLong(entry.getKey());
                        int value = entry.getValue(Integer.class);

                        // STEP 8: Add to appropriate list based on time
                        long sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L);

                        if (timestamp >= sevenDaysAgo) {
                            // Last 7 days - add to weekly
                            weeklyValues.add(value);
                        }

                        // Always add to monthly (we'll limit later)
                        monthlyValues.add(value);

                    } catch (Exception e) {
                        Log.e("PEF_FETCH", "Error: " + e.getMessage());
                    }
                }

                // STEP 9: Convert to arrays
                Number[] weeklyData = weeklyValues.toArray(new Number[0]);
                Number[] monthlyData = monthlyValues.toArray(new Number[0]);

                // STEP 10: Create date arrays (0, 1, 2, 3...)
                Number[] weeklyDates = new Number[weeklyData.length];
                Number[] monthlyDates = new Number[monthlyData.length];

                for (int i = 0; i < weeklyDates.length; i++) {
                    weeklyDates[i] = i;  // X positions: 0, 1, 2...
                }

                for (int i = 0; i < monthlyDates.length; i++) {
                    monthlyDates[i] = i;  // X positions: 0, 1, 2...
                }

                // STEP 11: Limit monthly to last 30 entries
                if (monthlyData.length > 30) {
                    monthlyData = Arrays.copyOfRange(monthlyData,
                            monthlyData.length - 30, monthlyData.length);
                    monthlyDates = new Number[30];
                    for (int i = 0; i < 30; i++) {
                        monthlyDates[i] = i;
                    }
                }

                // STEP 12: Return arrays through callback
                callback.onArraysReady(weeklyData, weeklyDates,
                        monthlyData, monthlyDates);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // STEP 13: Handle errors
                callback.onError("Failed to load: " + error.getMessage());
            }
        });
    }
}
