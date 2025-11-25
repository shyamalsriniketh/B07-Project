//this class is doing the wrong stuff but a lot of the code can be reused
//when parents go to share a child's data, the generating code stuff from here can be used

/*package com.example.smartair;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Random;

public class ParentToProviderInviteCodeActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    String currentInviteCode;
    Parent parent;
    long currentExpiry;
    FirebaseDatabase db;
    TextView display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        getParentData();

    }
    public void stopSharing(){
        String currentUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference parentRef = db.getReference("parents").child(currentUserId);
        parentRef.child("dataSharedWithProvider").setValue(false);
        parentRef.child("inviteCodeProvider").setValue(null);
        parent.setDataSharedWithProvider(false);
        parent.setInviteCodeProvider(null);
    }

    public void generateCode(){
        final long SEVEN_DAYS_IN_MILLIS = 7 * 24 * 60 * 60 * 1000L;
        String code = randomCode();
        long currTime = System.currentTimeMillis();
        currentExpiry = currTime + SEVEN_DAYS_IN_MILLIS;
        updateCodeDatabase(code, currentExpiry);
        currentInviteCode = code;
    }
    public String randomCode() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int CODE_LENGTH = 8;
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
    public void getParentData(){
        String currentUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference parentRef = db.getReference("parents").child(currentUserId);

        parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    parent = dataSnapshot.getValue(Parent.class);
                    currentInviteCode = dataSnapshot.child("inviteCodeProvider").getValue(String.class);
                    currentExpiry = dataSnapshot.child("providerCodeExpiry").getValue(long.class);
                    if (!parent.getDataSharedWithProvider() && parent != null) {
                        setContentView(R.layout.generate_invite_code_provider_fragment);
                        Button generate = findViewById(R.id.button2);
                        display = findViewById(R.id.textView4);
                        generate.setOnClickListener(v -> {
                            generateCode();
                            display.setText(currentInviteCode);
                            display.setVisibility(View.VISIBLE);
                        });
                    }
                    else {
                        setContentView(R.layout.parent_stop_sharing_with_provider_fragment);
                        Button stopSharing = findViewById(R.id.button5);
                        TextView textView = findViewById(R.id.textView6);
                        stopSharing.setOnClickListener(v -> {
                            stopSharing();
                            textView.setText("Disabled data sharing");
                        });
                    }


            }}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FIREBASE", "Error: " + databaseError.getMessage());
                Toast.makeText(ParentToProviderInviteCodeActivity.this,
                        "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateCodeDatabase(String code, long currentExpiry) {
        String currentUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference parentRef = db.getReference("parents").child(currentUserId);
        parentRef.child("providerCodeExpiry").setValue(currentExpiry);
        parentRef.child("inviteCodeProvider").setValue(code);
        parent.setInviteCodeProvider(code);
        parent.setProviderCodeExpiry(currentExpiry);
    }
}
*/