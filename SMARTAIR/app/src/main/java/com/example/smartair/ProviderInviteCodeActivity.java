package com.example.smartair;

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

public class ProviderInviteCodeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    //String currentInviteCode;
    //Parent parent;
    //long currentExpiry;
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
    /*
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
                        Boolean isSharing = dataSnapshot.child("dataSharedWithProvider").getValue(Boolean.class);
                        String currentCode = dataSnapshot.child("inviteCodeProvider").getValue(String.class);
                        Long expiry = dataSnapshot.child("providerCodeExpiry").getValue(Long.class);
                        boolean stillSharing = isSharing != null && isSharing && currentCode != null && expiry != null && System.currentTimeMillis() <= expiry;
                        if (stillSharing) {
                            stillSharingParentIds.add(parentId);
//                            Parent parent = dataSnapshot.getValue(Parent.class);
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
    */
    public void validateCode(String code, long date) {
        DatabaseReference childRef = db.getReference("children");
        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childId : dataSnapshot.getChildren()) {
                        Boolean isSharing = childId.child("dataSharedWithProvider").getValue(Boolean.class);
                        String currentCode = childId.child("inviteCodeProvider").getValue(String.class);
                        Long expiry = childId.child("providerCodeExpiry").getValue(Long.class);
                        boolean valid = isSharing != null && isSharing && currentCode != null && currentCode.equals(code) && expiry != null && date <= expiry;
                        if (valid) {
                            Toast.makeText(ProviderInviteCodeActivity.this, "Code accepted! Sharing report.", Toast.LENGTH_LONG).show();
                            //show report
                            return;
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
        /*
        DatabaseReference parentRef = db.getReference("parents");

        parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot parent_id : dataSnapshot.getChildren()) {
                        parent = parent_id.getValue(Parent.class);
                        currentInviteCode = parent_id.child("inviteCodeProvider").getValue(String.class);
                        currentExpiry = parent_id.child("providerCodeExpiry").getValue(long.class);
                        if (code != null && code.equals(currentInviteCode) && date <= currentExpiry) {
                            Toast.makeText(ProviderInviteCodeActivity.this, " Code accepted! Sharing data...", Toast.LENGTH_SHORT).show();
//                            shareDataWithProvider();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FIREBASE", "Error: " + databaseError.getMessage());
                Toast.makeText(ProviderInviteCodeActivity.this,
                        "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
         */
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
