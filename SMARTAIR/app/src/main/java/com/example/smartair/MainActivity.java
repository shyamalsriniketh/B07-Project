package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runChecks();
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

    private void runChecks() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childUid : snapshot.child("children").getChildren()) {
                    if (childUid.child("controllerToday").getValue(Boolean.class)) {
                        boolean takenController = false;
                        for (DataSnapshot controllerEntry : snapshot.child("logs").child(childUid.getKey()).child("controller").getChildren()) {
                            if (Long.parseLong(controllerEntry.getKey()) / (1000L * 60 * 60 * 24) == System.currentTimeMillis() / (1000L * 60 * 60 * 24)) {
                                takenController = true;
                                break;
                            }
                        }
                        if (!takenController) {
                            ref.child("logs").child(childUid.getKey()).child("controller").child(String.valueOf(System.currentTimeMillis())).setValue("No entry today");
                        }
                    }
                    boolean enteredPEF = false;
                    for (DataSnapshot pefEntry : snapshot.child("logs").child(childUid.getKey()).child("pef").getChildren()) {
                        if (Long.parseLong(pefEntry.getKey()) / (1000L * 60 * 60 * 24) == System.currentTimeMillis() / (1000L * 60 * 60 * 24)) {
                            enteredPEF = true;
                            break;
                        }
                    }
                    if (!enteredPEF) {
                        ref.child("logs").child(childUid.getKey()).child("pef").child(String.valueOf(System.currentTimeMillis())).setValue("No entry today");
                    }
                }

                DataSnapshot child;
                Date curDate = new Date();
                DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                for (DataSnapshot parentUid : snapshot.child("parents").getChildren()) {
                    for (DataSnapshot childUid : parentUid.child("linkedChildren").getChildren()) {
                        child = snapshot.child("children").child(childUid.getValue(String.class));
                        try {
                            if (curDate.before(sdf.parse(child.child("rescueExpiryDate").getValue(String.class)))
                                    || curDate.before(sdf.parse(child.child("controllerExpiryDate").getValue(String.class)))) {
                                ref.child("alerts").child(parentUid.getKey()).child("inventoryExpired").setValue(true);
                                return;
                            }
                        } catch (ParseException e) {
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}