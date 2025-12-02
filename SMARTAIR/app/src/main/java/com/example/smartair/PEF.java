package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.util.ArrayList;


public class PEF extends AppCompatActivity {

    Button backButton, nextButton;
    DatabaseReference reference;
    EditText pef;
    ArrayList<Integer> pefValues = new ArrayList<>();
    ArrayList<Long> pefTimestamps = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pef);
        reference = FirebaseDatabase.getInstance().getReference().child("logs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pef");
        pef = findViewById(R.id.PEF_editText);
        backButton = findViewById(R.id.PEF_Back_Button);
        nextButton = findViewById(R.id.PEF_Next_Button);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PEF.this, Child_Input.class);
            if (getIntent().hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) getIntent().getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });


        nextButton.setOnClickListener(v -> {
            String manualPef = pef.getText().toString();
            if(manualPef.isEmpty()){
                Toast.makeText(PEF.this, "Please fill in your PEF", Toast.LENGTH_SHORT).show();
                return;
            }

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot pefEntries : snapshot.getChildren()) {
                        if (Long.parseLong(pefEntries.getKey()) / (1000L * 60 * 60 * 24) == System.currentTimeMillis() / (1000L * 60 * 60 * 24) && String.valueOf(pefEntries.getValue(Object.class)).equals("No entry today")) {
                            reference.child(pefEntries.getKey()).removeValue();
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });

            int p = Integer.parseInt(manualPef);
            long timestamp = System.currentTimeMillis();
            reference.child(String.valueOf(timestamp)).setValue(p);
            pefValues.add(p);
            pefTimestamps.add(timestamp);


            Intent intent = new Intent(PEF.this, Child_Input.class);
            if (getIntent().hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) getIntent().getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });
    }
}
