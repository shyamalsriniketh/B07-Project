package com.example.smartair;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProviderInviteCodeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    String currentInviteCode;
    Parent parent;
    long currentExpiry;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String currentUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference parentRef = db.getReference("parents").child(currentUserId);
        setContentView(R.layout.enter_invite_code_provider_fragment);
        EditText edittext = findViewById(R.id.textInputEditText);
        Button done=  findViewById(R.id.button3);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth= FirebaseAuth.getInstance();
                db= FirebaseDatabase.getInstance();
                String code = edittext.getText().toString();
                long date=System.currentTimeMillis();
                ValidateCode(code, date);
            }

        });;

    }
    public void loadProviderAccessibleParents() {
        String providerId = mAuth.getCurrentUser().getUid();
        DatabaseReference providerRef = db.getReference("providers")
                .child(providerId)
                .child("accessibleParents");

        providerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> parentIds = new ArrayList<>();
                for (DataSnapshot parentSnapshot : dataSnapshot.getChildren()) {
                    String parentId = parentSnapshot.getKey();
                    parentIds.add(parentId);
                }
                checkSharingStatusAndLoadParents(parentIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PROVIDER", "Error loading accessible parents: " + error.getMessage());
            }
        });

    }
    public void checkSharingStatusAndLoadParents(List<String> parentIds){
        DatabaseReference parentsRef = db.getReference("parents");
        List<String> stillSharingParentIds = new ArrayList<>();

        for (String parentId : parentIds) {
            parentsRef.child(parentId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Boolean isSharing = dataSnapshot.child("DataSharedWithProvider").getValue(Boolean.class);
                        String currentCode = dataSnapshot.child("invitecodeProvider").getValue(String.class);
                        Long expiry = dataSnapshot.child("ProviderCodeExpiry").getValue(Long.class);
                        boolean stillSharing = isSharing != null && isSharing && currentCode != null && expiry != null && System.currentTimeMillis() <= expiry;
                        if (stillSharing) {
                            stillSharingParentIds.add(parentId);
                            Parent parent = dataSnapshot.getValue(Parent.class);
//                            displayParentData(parent);
                        } else {
                            removeParentFromProvider(parentId);
                            Log.d("SHARING_STATUS", "Parent " + parentId + " stopped sharing");
                        }
                    } else {
                        removeParentFromProvider(parentId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FIREBASE", "Error: " + error.getMessage());
                    Toast.makeText(ProviderInviteCodeActivity.this,
                            "Failed to share data", Toast.LENGTH_SHORT).show();

                }
            });
        }



    }
    public void removeParentFromProvider(String parentId){
        String providerId = mAuth.getCurrentUser().getUid();
        DatabaseReference providerRef = db.getReference("providers")
                .child(providerId)
                .child("accessibleParents")
                .child(parentId);

        providerRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d("REMOVE_PARENT", "Removed parent " + parentId + " - they stopped sharing");
                })
                .addOnFailureListener(e -> {
                    Log.e("REMOVE_PARENT", "Failed to remove parent: " + e.getMessage());
                });
    }

    public void ValidateCode(String code, long date) {
        String currentUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference parentRef = db.getReference("parents").child(currentUserId);

        parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    parent = dataSnapshot.getValue(Parent.class);
                    currentInviteCode = dataSnapshot.child("invitecodeProvider").getValue(String.class);
                    currentExpiry = dataSnapshot.child("ProviderCodeExpiry").getValue(long.class);
                    if (code.equals(currentInviteCode) && date <= currentExpiry && !(code.equals(null))) {
                        Toast.makeText(ProviderInviteCodeActivity.this, " Code accepted! Sharing data...", Toast.LENGTH_SHORT).show();
//                        shareDataWithProvider();
                    } else {
                        if (!code.equals(currentInviteCode)) {
                            Toast.makeText(ProviderInviteCodeActivity.this,
                                    "Invalid code", Toast.LENGTH_SHORT).show();
                        } else if (date > currentExpiry) {
                            Toast.makeText(ProviderInviteCodeActivity.this,
                                    "Code has expired", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FIREBASE", "Error: " + databaseError.getMessage());
                Toast.makeText(ProviderInviteCodeActivity.this,
                        "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
//    public void displayParentData(Parent parent) {
//        if (parent != null) {
//            //not sure how we will display it on the screen yet
//        }
//    }
//    public void shareDataWithProvider(){
//        //is empty because im not sure what the data is yet
//    }


}
