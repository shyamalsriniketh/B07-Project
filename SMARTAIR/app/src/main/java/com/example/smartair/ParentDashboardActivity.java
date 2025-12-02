package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParentDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);
        displayAlerts();
        Button button = findViewById(R.id.button3);
        button.setOnClickListener(v -> {
            Intent i = new Intent(this, ManageChildrenActivity.class);
            startActivity(i);
        });
        Button button2 = findViewById(R.id.button6);
        button2.setOnClickListener(v -> {
            Intent i = new Intent(this, SignOut.class);
            startActivity(i);
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