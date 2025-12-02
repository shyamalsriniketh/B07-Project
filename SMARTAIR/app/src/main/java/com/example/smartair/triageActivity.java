package com.example.smartair;

import android.content.Intent;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

//firebase imports
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;


public class triageActivity extends AppCompatActivity {
    String category;
    boolean escalation = false;
    String chosenPlan = "";





    //variables from child class and firebase
    private int pbValue;
    private String greenPlan;
    private String yellowPlan;
    private String redPlan;

    private Child currentChild;
    private String childUid;

    private DatabaseReference db;
    private FirebaseAuth auth;



    //variables to detect escalation and red flag

    boolean worseSymptoms;
    CheckBox cbCantSpeak, cbChestPulling, cbBlueLips;
    Button btnDecision;

    EditText editRescueAttempts, editPEF;

    //variables for escalation

    private SharedPreferences prefs;
    private static final String PREFS_NAME = "triageMemory";
    private static final String KEY_LAST_CANT_SPEAK = "last_cant_speak";
    private static final String KEY_LAST_CHEST = "last_chest";
    private static final String KEY_LAST_BLUE = "last_blue";
    private static final String KEY_LAST_PEF = "last_pef";
    private static final String KEY_LAST_RESCUE = "last_rescue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_triage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.triage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        cbCantSpeak = findViewById(R.id.checkbox_cant_speak);
        cbChestPulling = findViewById(R.id.checkbox_chest_pulling);
        cbBlueLips = findViewById(R.id.checkbox_blue_lips);
        btnDecision = findViewById(R.id.btn_decision);
        editRescueAttempts = findViewById(R.id.edit_rescue_attempts);
        editPEF = findViewById(R.id.edit_pef);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        //firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        childUid = auth.getCurrentUser().getUid();  // parent or child login

        loadChildData();


        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> updateButtonState();
        cbCantSpeak.setOnCheckedChangeListener(listener);
        cbChestPulling.setOnCheckedChangeListener(listener);
        cbBlueLips.setOnCheckedChangeListener(listener);
        updateButtonState();
        escalation = false;
        chosenPlan = "";

    }
    private void updateButtonState() {
        boolean redFlag = cbCantSpeak.isChecked()
                || cbChestPulling.isChecked()
                || cbBlueLips.isChecked();
        worseSymptoms = redFlag;

        if (redFlag) {
            btnDecision.setText("CALL EMERGENCY NOW");
            btnDecision.setBackgroundColor(Color.RED);
            btnDecision.setTextColor(Color.WHITE);
        } else {
            btnDecision.setText("START HOME STEPS");
            btnDecision.setBackgroundColor(Color.parseColor("#6200EE"));
            btnDecision.setTextColor(Color.WHITE);
        }

    }
    public void handleDecisionClick(View v) {
        //variables for incident log,




        boolean nowCantSpeak = cbCantSpeak.isChecked();
        boolean nowChest = cbChestPulling.isChecked();
        boolean nowBlue = cbBlueLips.isChecked();
        int nowRescue = parseIntSafe(editRescueAttempts.getText().toString(), 0);









        int nowPEF = parseIntSafe(editPEF.getText().toString(), -1);
        if(nowPEF == -1) {
            loadLatestPEF(childUid, latest -> {
                int fixedPEF = (latest != -1) ? latest : pbValue;
                finishDecisionWithPEF(fixedPEF,nowCantSpeak,nowChest,nowBlue,nowRescue);
            });
            return;
        }
        finishDecisionWithPEF(nowPEF,nowCantSpeak,nowChest,nowBlue,nowRescue);









        String text = btnDecision.getText().toString();

        createTriageAlert(childUid,escalation);





        // pef logic till here


        if (text.equals("START HOME STEPS")) {
            Intent i = new Intent(this, homeStepsActivity.class);
            // add steps




            i.putExtra("planText", chosenPlan);

            startActivity(i);
        }
        else{
            Intent i = new Intent(this,triageActivity.class);
            getSharedPreferences("flags",MODE_PRIVATE)
                    .edit().putBoolean("start_home_after_emergency", true).apply();



            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:911"));
            startActivity(dialIntent);



        }
    }

    private void saveCurrentValues(boolean cantSpeak, boolean chest, boolean blue, int pef, int rescue) {
        prefs.edit()
                .putBoolean(KEY_LAST_CANT_SPEAK, cantSpeak)
                .putBoolean(KEY_LAST_CHEST, chest)
                .putBoolean(KEY_LAST_BLUE, blue)
                .putInt(KEY_LAST_PEF, pef)
                .putInt(KEY_LAST_RESCUE, rescue)
                .apply();
    }

    /**
     * Helper: parse integer safely, return default if empty or invalid.
     */
    private int parseIntSafe(String s, int defaultValue) {
        if (s == null || s.trim().isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        int nowPEF = parseIntSafe(editPEF.getText().toString(), -1);

        boolean shouldStartHome =
                getSharedPreferences("flags", MODE_PRIVATE)
                        .getBoolean("start_home_after_emergency", false);

        if (shouldStartHome) {


            getSharedPreferences("flags", MODE_PRIVATE)
                    .edit()
                    .putBoolean("start_home_after_emergency", false)
                    .apply();

            Intent i = new Intent(this, homeStepsActivity.class);
            String chosenPlan = chooseActionPlan(nowPEF);





            i.putExtra("planText", chosenPlan);

            startActivity(i);
        }
    }







    //firebase loadChild
    private void loadChildData() {
        db.child("children").child(childUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            currentChild = snapshot.getValue(Child.class);

                            if (currentChild != null) {
                                pbValue = currentChild.getPb();
                                greenPlan = currentChild.getGreenActionPlan();
                                yellowPlan = currentChild.getYellowActionPlan();
                                redPlan = currentChild.getRedActionPlan();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) { }
                });
    }
    private String chooseActionPlan(int pef) {


        if (pef == -1) {
            pef = pbValue;

        }

        if (pbValue > 0) {
            double percent = (100.0 * pef) / pbValue;

            if (percent >= 80) {
                category = "Green";
                return greenPlan;
            } else if (percent >= 50) {
                category = "Yellow";
                return yellowPlan;
            } else {
                category = "Red";
                return redPlan;
            }
        }

        return greenPlan; // fallback
    }
    private void saveIncidentLogToFirebase(IncidentLog log) {
        long now = System.currentTimeMillis();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("logs")
                .child(log.ChildUid)
                .child("incidentLogs");

        ref.child(String.valueOf(now)).setValue(log);

    }
    private void findParentUid(String childUid, ParentUidCallback callback) {
        DatabaseReference parentsRef = FirebaseDatabase.getInstance()
                .getReference("parents");

        parentsRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful() || task.getResult() == null) {
                callback.onResult(null);
                return;
            }

            for (DataSnapshot parentSnap : task.getResult().getChildren()) {

                DataSnapshot linkedChildren = parentSnap.child("linkedChildren");

                for (DataSnapshot childSnap : linkedChildren.getChildren()) {
                    String id = childSnap.child("id").getValue(String.class);

                    if (childUid.equals(id)) {
                        callback.onResult(parentSnap.getKey());
                        return;
                    }
                }
            }

            callback.onResult(null); // none found
        });
    }
    public interface ParentUidCallback {
        void onResult(String parentUid);
    }

    private void createTriageAlert(String childUid, boolean escalation) {

        // First get the child's name
        getChildName(childUid, childName -> {

            // Then find parent UID
            findParentUid(childUid, parentUid -> {

                if (parentUid == null) return;  // no parent found

                String timestamp = new SimpleDateFormat("HH:mm",
                        Locale.getDefault()).format(new Date());

                String message;

                if (escalation) {
                    message = "Escalation for " + childName + " at " + timestamp;
                } else {
                    message = "Triage started for " + childName + " at " + timestamp;
                }

                DatabaseReference alertRef = FirebaseDatabase.getInstance()
                        .getReference("alerts")
                        .child(parentUid)
                        .child("triageAlerts");

                alertRef.push().setValue(message);
            });
        });
    }

    private void getChildName(String childUid, OnChildNameListener listener) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("children")
                .child(childUid)
                .child("name");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String name = snapshot.getValue(String.class);
                listener.onGotName(name);
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
    interface OnChildNameListener {
        void onGotName(String name);
    }
    private void loadLatestPEF(String childUid, LatestPefCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("logs")
                .child(childUid)
                .child("pef");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int latestPef = -1;
                long latestTime = -1;

                for (DataSnapshot s : snapshot.getChildren()) {
                    try {
                        long timestamp = Long.parseLong(s.getKey());
                        int pefValue = s.getValue(Integer.class);

                        if (timestamp > latestTime) {
                            latestTime = timestamp;
                            latestPef = pefValue;
                        }
                    } catch (Exception e) {
                        // ignore bad data
                    }
                }

                callback.onResult(latestPef);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onResult(-1);
            }
        });
    }
    private interface LatestPefCallback {
        void onResult(int pef);
    }



    private void finishDecisionWithPEF(int nowPEF,boolean nowCantSpeak, boolean nowChest, boolean nowBlue, int nowRescue) {
        boolean isRecheck = getIntent().getBooleanExtra("recheck", false);
        int lastPEF = prefs.getInt(KEY_LAST_PEF, -1);
        boolean pefWorse = (lastPEF != -1 && nowPEF != -1 && nowPEF < lastPEF);
        escalation = isRecheck && (worseSymptoms || pefWorse);
        saveCurrentValues(nowCantSpeak, nowChest, nowBlue, nowPEF, nowRescue);
        chosenPlan = chooseActionPlan(nowPEF);
        if (worseSymptoms) category = "Emergency";
        IncidentLog I = new IncidentLog(category,nowCantSpeak,nowChest,nowBlue,nowRescue,nowPEF,chosenPlan,childUid);
        saveIncidentLogToFirebase(I);
    }














}







