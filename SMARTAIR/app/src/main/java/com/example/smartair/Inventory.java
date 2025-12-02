package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Inventory extends AppCompatActivity {

    TextView rescueTitle;
    TextView rescuePurchase;
    TextView rescueExpiry;
    TextView controllerPurchase;
    TextView controllerExpiry;
    TextView rescueLeft;
    TextView controllerLeft;
    TextView childMarked;
    Button dismiss;
    Button back;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        rescueTitle = findViewById(R.id.inventory_title);
        rescuePurchase = findViewById(R.id.rescue_purchase);
        rescueExpiry = findViewById(R.id.rescue_expiry);
        controllerPurchase = findViewById(R.id.controller_purchase);
        controllerExpiry = findViewById(R.id.controller_expiry);
        rescueLeft = findViewById(R.id.rescue_left);
        controllerLeft = findViewById(R.id.controller_left);
        childMarked = findViewById(R.id.child_marked);
        dismiss = findViewById(R.id.dismiss_child_mark);
        back = findViewById(R.id.button14);
        Intent i = getIntent();
        String childUID = i.getStringExtra("CHILD_UID");
        reference = FirebaseDatabase.getInstance().getReference("children").child(childUID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rescueTitle.setText("Viewing inventory for " + snapshot.child("id").getValue(String.class));
                rescuePurchase.setText("Rescue last purchased: " + snapshot.child("rescuePurchaseDate").getValue(String.class));
                rescueExpiry.setText("Last rescue purchased expires on: " + snapshot.child("rescueExpiryDate").getValue(String.class));
                controllerPurchase.setText("Controller last purchased: " + snapshot.child("controllerPurchaseDate").getValue(String.class));
                controllerExpiry.setText("Last controller purchased expires on: " + snapshot.child("controllerExpiryDate").getValue(String.class));
                rescueLeft.setText("Percentage of rescue medicine left in inventory (parent-marked): " + snapshot.child("rescueLeft").getValue(Double.class) + "%");
                controllerLeft.setText("Percentage of controller medicine left in inventory (parent-marked): " + snapshot.child("controllerLeft").getValue(Double.class) + "%");
                if (snapshot.child("inventoryMarkedLow").getValue(Boolean.class)) {
                    childMarked.setVisibility(View.VISIBLE);
                    dismiss.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        dismiss.setOnClickListener(v -> {
            reference.child("inventoryMarkedLow").setValue(false);
            childMarked.setVisibility(View.INVISIBLE);
            dismiss.setVisibility(View.INVISIBLE);
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(Inventory.this, ViewChildActivity.class);
            startActivity(intent);
        });
    }
}