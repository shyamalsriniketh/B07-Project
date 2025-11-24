package com.example.smartair;


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
        GetParentData();

    }
    public void StopSharing(){
        String currentUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference parentRef = db.getReference("parents").child(currentUserId);
        parentRef.child("DataSharedWithProvider").setValue(false);
        parentRef.child("invitecodeProvider").setValue(null);
        parent.DataSharedWithProvider=false;
        parent.invitecodeProvider=null;

    }

    public void GenerateCode(){
        final long SEVEN_DAYS_IN_MILLIS = 7 * 24 * 60 * 60 * 1000L;
        String code= RandomCode();
        long currtime=System.currentTimeMillis();
        currentExpiry = currtime + SEVEN_DAYS_IN_MILLIS;
        parent.updateInvite(code, currentExpiry);
        UpdateCodeDatabase(code, currentExpiry);
        currentInviteCode= code;
    }
    public String RandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }
    public void GetParentData(){

        String currentUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference parentRef = db.getReference("parents").child(currentUserId);

        parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    parent = dataSnapshot.getValue(Parent.class);
                    currentInviteCode = dataSnapshot.child("invitecodeProvider").getValue(String.class);
                    currentExpiry = dataSnapshot.child("ProviderCodeExpiry").getValue(long.class);
                    if (!parent.DataSharedWithProvider && parent!=null) {
                        setContentView(R.layout.generate_invite_code_provider_fragment);
                        Button generate = findViewById(R.id.button2);
                        display = findViewById(R.id.textView4);
                        generate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GenerateCode();
                                display.setText(currentInviteCode);
                            }

                        });
                    }
                    else {
                        setContentView(R.layout.parent_stop_sharing_with_provider_fragment);
                        Button stopsharing= findViewById(R.id.button5);
                        TextView textView = findViewById(R.id.textView6);
                        stopsharing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                StopSharing();
                                textView.setText("Disabled data sharing");
                            }

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
    public void UpdateCodeDatabase(String code, long currentExpiry) {
        String currentUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference parentRef = db.getReference("parents").child(currentUserId);
        parentRef.child("ProviderCodeExpiry").setValue(currentExpiry);
        parentRef.child("invitecodeProvider").setValue(code);
    }
}
