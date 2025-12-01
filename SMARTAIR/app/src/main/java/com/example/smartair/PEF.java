package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class PEF extends AppCompatActivity {

    Button backButton, nextButton;
    DatabaseReference reference;
    EditText pef;


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

            int p = Integer.parseInt(manualPef);
            long timestamp = System.currentTimeMillis();
            reference.child(String.valueOf(timestamp)).setValue(p);

            Intent intent = new Intent(PEF.this, Child_Input.class);
            if (getIntent().hasExtra("PARENT_VIEW")) {
                intent.putExtra("PARENT_VIEW", (Parcelable) getIntent().getParcelableExtra("PARENT_VIEW"));
            }
            startActivity(intent);
        });
    }
}
