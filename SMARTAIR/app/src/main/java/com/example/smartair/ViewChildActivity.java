package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewChildActivity extends AppCompatActivity {

    Spinner spinner;
    DatabaseReference reference;
    FirebaseUser user;
    Button save;
    ListView list;
    TextView dataName;
    EditText dataInput;
    Button viewAsChild;
    Button shareWithProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_child);
        spinner = findViewById(R.id.child_spinner);
        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        save = findViewById(R.id.save_button);
        list = findViewById(R.id.data_list);
        dataName = findViewById(R.id.data_name);
        dataInput = findViewById(R.id.data_entry);
        viewAsChild = findViewById(R.id.view_as_child);
        shareWithProvider = findViewById(R.id.share_with_provider);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> children_usernames;
                ArrayAdapter<String> adapter;
                if (snapshot.child("parents").child(user.getUid()).child("linkedChildren").getValue() == null) {
                    Toast.makeText(ViewChildActivity.this, "You don't have any children linked to your account!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ViewChildActivity.this, ManageChildrenActivity.class);
                    startActivity(i);
                }
                children_usernames = new ArrayList<>();
                for (DataSnapshot snapshots : snapshot.child("parents").child(user.getUid()).child("linkedChildren").getChildren()) {
                    children_usernames.add(snapshot.child("children").child(snapshots.getValue().toString()).child("id").getValue().toString());
                }
                adapter = new ArrayAdapter<>(ViewChildActivity.this, android.R.layout.simple_spinner_item, children_usernames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewChildActivity.this, "Error: couldn't display children", Toast.LENGTH_LONG).show();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ViewChildActivity.this, "Click on an item to edit its value! Scroll to see more", Toast.LENGTH_LONG).show();
                displayChildData(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void displayChildData(String username) {
        ArrayList<String> data = new ArrayList<>();
        reference.child("children").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot snapshots = null;
                for (DataSnapshot shotChild : snapshot.getChildren()) {
                    if (shotChild.child("id").getValue().toString().equals(username)) {
                        snapshots = shotChild;
                        break;
                    }
                }
                for (DataSnapshot attributes : snapshots.getChildren()) {
                    String key = attributes.getKey();
                    if (!key.equals("onboarded") && !key.equals("id") && !key.equals("password")
                            && !key.equals("inviteCodeProvider") && !key.equals("providerCodeExpiry")) {
                        data.add(key + ": " + snapshots.child(key).getValue(Object.class));
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewChildActivity.this, android.R.layout.simple_list_item_1, data);
                list.setAdapter(adapter);
                list.setVisibility(View.VISIBLE);
                DataSnapshot finalSnapshots = snapshots;
                viewAsChild.setOnClickListener(v -> {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(finalSnapshots.child("id").getValue(String.class) + AddChildActivity.DOMAIN, finalSnapshots.child("password").getValue(String.class)).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(ViewChildActivity.this, ChildDashboardActivity.class);
                            i.putExtra("PARENT_VIEW", user);
                            startActivity(i);
                        }
                    });
                });
                shareWithProvider.setOnClickListener(v -> {
                    Intent i = new Intent(ViewChildActivity.this, InvitingProviderActivity.class); //should go to screen where toggles are first
                    i.putExtra("CHILD_UID", finalSnapshots.getKey());
                    startActivity(i);
                });
                list.setOnItemClickListener((parent, view, position, id) -> {
                    String keyAndValue = parent.getItemAtPosition(position).toString();
                    String key = keyAndValue.split(":")[0];
                    dataName.setText("Editing " + key);
                    dataInput.setText("");
                    switch (key) {
                        case "age":
                        case "pb":
                            dataInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                            break;
                        case "highQualitySessionNum":
                            dataInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                            dataName.setText("Editing number of high quality sessions required for a badge");
                            break;
                        case "lowRescueMonthNum":
                            dataInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                            dataName.setText("Editing maximum number of rescue days in a month to earn a badge");
                            break;
                        case "greenActionPlan":
                            dataInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            dataInput.setSingleLine(false);
                            dataInput.setMaxLines(10);
                            dataName.setText("Editing action plan shown to child when zone is green");
                            break;
                        case "yellowActionPlan":
                            dataInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            dataInput.setSingleLine(false);
                            dataInput.setMaxLines(10);
                            dataName.setText("Editing action plan shown to child when zone is yellow");
                            break;
                        case "redActionPlan":
                            dataInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            dataInput.setSingleLine(false);
                            dataInput.setMaxLines(10);
                            dataName.setText("Editing action plan shown to child when zone is red");
                            break;
                        case "additionalNotes":
                            dataInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            dataInput.setSingleLine(false);
                            dataInput.setMaxLines(10);
                            break;
                        case "controllerToday":
                            dataInput.setInputType(InputType.TYPE_CLASS_TEXT);
                            dataName.setText("Editing whether child should take a controller dose today (true or false)");
                            break;
                        case "rescueLeft":
                            dataInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                            dataName.setText("Editing percentage of rescue medicine left in inventory");
                            break;
                        case "controllerLeft":
                            dataInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                            dataName.setText("Editing percentage of controller medicine left in inventory");
                            break;
                        case "rescuePurchaseDate":
                            dataInput.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
                            dataName.setText("Editing date of last rescue medicine purchase (MM-DD-YYYY)");
                            break;
                        case "rescueExpiryDate":
                            dataInput.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
                            dataName.setText("Editing expiry date of last rescue medicine purchase (MM-DD-YYYY)");
                            break;
                        case "controllerPurchaseDate":
                            dataInput.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
                            dataName.setText("Editing date of last controller medicine purchase (MM-DD-YYYY)");
                            break;
                        case "controllerExpiryDate":
                            dataInput.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
                            dataName.setText("Editing expiry date of last controller medicine purchase (MM-DD-YYYY)");
                            break;
                        default:
                            dataInput.setInputType(InputType.TYPE_CLASS_TEXT);
                            break;
                    }
                    list.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    dataName.setVisibility(View.VISIBLE);
                    dataInput.setVisibility(View.VISIBLE);
                    save.setVisibility(View.VISIBLE);
                    viewAsChild.setVisibility(View.INVISIBLE);
                    shareWithProvider.setVisibility(View.INVISIBLE);
                    save.setOnClickListener(v -> {
                        if (!dataInput.getText().toString().isEmpty()) {
                            switch (key) {
                                case "age":
                                case "pb":
                                case "highQualitySessionNum":
                                case "lowRescueMonthNum":
                                    int number = Integer.parseInt(dataInput.getText().toString());
                                    if ((key.equals("age") && number >= 6 && number <= 16)
                                            || (key.equals("pb") && number > 0)
                                            || (key.equals("highQualitySessionNum") && number >= 0)
                                            || (key.equals("lowRescueMonthNum") && number >= 0 && number <= 30)) {
                                        reference.child("children").child(finalSnapshots.getKey()).child(key).setValue(number);
                                    }
                                    else {
                                        Toast.makeText(ViewChildActivity.this, "Invalid input", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                case "rescueLeft":
                                case "controllerLeft":
                                    double percent = Double.parseDouble(dataInput.getText().toString());
                                    if (percent >= 0 && percent <= 100) {
                                        reference.child("children").child(finalSnapshots.getKey()).child(key).setValue(percent);
                                    }
                                    else {
                                        Toast.makeText(ViewChildActivity.this, "Invalid input", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                case "controllerToday":
                                    String input = dataInput.getText().toString().toLowerCase();
                                    if (input.equals("true") || input.equals("false")) {
                                        reference.child("children").child(finalSnapshots.getKey()).child(key).setValue(input.equals("true"));
                                    }
                                    else {
                                        Toast.makeText(ViewChildActivity.this, "Invalid input", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                case "rescuePurchaseDate":
                                case "controllerPurchaseDate":
                                    String pDate = dataInput.getText().toString();
                                    if (isValidPurchase(pDate)) {
                                        reference.child("children").child(finalSnapshots.getKey()).child(key).setValue(pDate);
                                    }
                                    else {
                                        Toast.makeText(ViewChildActivity.this, "Invalid input", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                case "rescueExpiryDate":
                                case "controllerExpiryDate":
                                    String eDate = dataInput.getText().toString();
                                    if (isValidExpiry(eDate)) {
                                        reference.child("children").child(finalSnapshots.getKey()).child(key).setValue(eDate);
                                    }
                                    else {
                                        Toast.makeText(ViewChildActivity.this, "Invalid input", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                default:
                                    reference.child("children").child(finalSnapshots.getKey()).child(key).setValue(dataInput.getText().toString());
                                    break;
                            }
                        }
                        dataName.setVisibility(View.INVISIBLE);
                        dataInput.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.INVISIBLE);
                        list.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                        viewAsChild.setVisibility(View.VISIBLE);
                        shareWithProvider.setVisibility(View.VISIBLE);
                    });
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private static boolean isValidPurchase(String date) {
        DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        sdf.setLenient(false);
        try {
            Date enteredDate = sdf.parse(date);
            Date curDate = new Date();
            return (enteredDate.before(curDate) || enteredDate.equals(curDate));
        }
        catch (ParseException e) {
            return false;
        }
    }

    private static boolean isValidExpiry(String date) {
        DateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        sdf.setLenient(false);
        try {
            Date enteredDate = sdf.parse(date);
            Date curDate = new Date();
            return curDate.before(enteredDate);
        }
        catch (ParseException e) {
            return false;
        }
    }
}