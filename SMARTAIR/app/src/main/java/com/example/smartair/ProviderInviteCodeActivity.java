package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProviderInviteCodeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        setContentView(R.layout.enter_invite_code_provider_fragment);
        EditText editText = findViewById(R.id.textInputEditText);
        Button done = findViewById(R.id.button3);
        done.setOnClickListener(v -> {
            String code = editText.getText().toString();
            long date = System.currentTimeMillis();
            validateCode(code, date);
        });

    }
    public void validateCode(String code, long date) {
        DatabaseReference ref = db.getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childId : dataSnapshot.child("children").getChildren()) {
                        String currentCode = childId.child("inviteCodeProvider").getValue(String.class);
                        Long expiry = childId.child("providerCodeExpiry").getValue(Long.class);
                        boolean valid = code.equals(currentCode) && date <= expiry;
                        if (valid) {
                            HashMap<String, String> childrenAndCodes;
                            if (!childId.hasChild("childrenAndCodes")) {
                                childrenAndCodes = new HashMap<>();
                            }
                            else {
                                childrenAndCodes = dataSnapshot.child("providers").child(mAuth.getCurrentUser().getUid()).child("childrenAndCodes").getValue(HashMap.class);
                            }
                            childrenAndCodes.put(code, childId.getKey());
                            ref.child("providers").child(mAuth.getCurrentUser().getUid()).child("childrenAndCodes").setValue(childrenAndCodes);
                            Toast.makeText(ProviderInviteCodeActivity.this, "Code accepted! Sharing report.", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(ProviderInviteCodeActivity.this, ProviderViewReports.class);
                            startActivity(i);
                        }
                    }
                }
                Toast.makeText(ProviderInviteCodeActivity.this, "Invalid code.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE", "Error: " + error.getMessage());
                Toast.makeText(ProviderInviteCodeActivity.this,
                        "Failed to validate code", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
