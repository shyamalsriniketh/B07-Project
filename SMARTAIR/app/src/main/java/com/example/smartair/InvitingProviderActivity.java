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

import java.util.Random;

public class InvitingProviderActivity extends AppCompatActivity {
    String childUid;
    Child child;
    DatabaseReference reference;
    Button generate;
    Button regenerate;
    Button revoke;
    TextView title;
    TextView displayCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inviting_provider);
        reference = FirebaseDatabase.getInstance().getReference();
        generate = findViewById(R.id.generate_button);
        regenerate = findViewById(R.id.regenerate_button);
        revoke = findViewById(R.id.revoke_button);
        title = findViewById(R.id.sharing_title);
        displayCode = findViewById(R.id.display_code);
        Intent i = getIntent(); //intent should be created with extra String for the selected child's username
        childUid = i.getStringExtra("CHILD_UID");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                child = snapshot.child("children").child(childUid).getValue(Child.class);
                title.setText("Sharing " + child.getId() + "'s data:");
                if (child.getProviderCodeExpiry() >= System.currentTimeMillis()) {
                    generate.setVisibility(View.INVISIBLE);
                    displayCode.setText("Current code: " + child.getInviteCodeProvider());
                    displayCode.setVisibility(View.VISIBLE);
                    regenerate.setVisibility(View.VISIBLE);
                    revoke.setVisibility(View.VISIBLE);
                }
                generate.setOnClickListener(v -> generateCode());
                regenerate.setOnClickListener(v -> generateCode());
                revoke.setOnClickListener(v -> revokeCode());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void generateCode(){
        final long SEVEN_DAYS_IN_MILLIS = 7 * 24 * 60 * 60 * 1000L;
        String code = randomCode();
        long currTime = System.currentTimeMillis();
        long expiry = currTime + SEVEN_DAYS_IN_MILLIS;
        child.setInviteCodeProvider(code);
        child.setProviderCodeExpiry(expiry);
        reference.child("children").child(childUid).setValue(child);
        generate.setVisibility(View.INVISIBLE);
        displayCode.setText("Current code: " + child.getInviteCodeProvider());
        displayCode.setVisibility(View.VISIBLE);
        regenerate.setVisibility(View.VISIBLE);
        revoke.setVisibility(View.VISIBLE);
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

    public void revokeCode() {
        child.setInviteCodeProvider(null);
        child.setProviderCodeExpiry(0);
        reference.child("children").child(childUid).setValue(child);
        displayCode.setVisibility(View.INVISIBLE);
        regenerate.setVisibility(View.INVISIBLE);
        revoke.setVisibility(View.INVISIBLE);
        generate.setVisibility(View.VISIBLE);
    }
}