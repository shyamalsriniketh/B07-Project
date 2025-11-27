package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProviderViewReports extends AppCompatActivity {
    FirebaseAuth mAuth;
    String providerId;
    DatabaseReference reference;

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
                for (DataSnapshot code : snapshot.child("providers").child(providerId).child("childrenAndCodes").getChildren()) {
                    if (code.getKey().equals(snapshot.child("children").child(code.getValue(String.class)).child("inviteCodeProvider").getValue(String.class))) {
                        //display child's report
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
}