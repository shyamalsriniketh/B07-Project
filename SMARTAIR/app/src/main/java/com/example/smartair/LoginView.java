package com.example.smartair;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginView extends AppCompatActivity {

    EditText editTextEmailUsername, editTextPassword;
    Button buttonLogin;
    TextView textView;
    LoginPresenter presenter;

    @Override
    public void onStart() {
        super.onStart();
        presenter.determineScreen();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmailUsername = findViewById(R.id.enterEmailOrUsername);
        editTextPassword = findViewById(R.id.enterPassword);
        buttonLogin = findViewById(R.id.button_Login);
        textView = findViewById(R.id.registerNow);
        presenter = new LoginPresenter(new LoginModel(), this);
        textView.setOnClickListener(view -> presenter.handleRoleSelectionClick());
        buttonLogin.setOnClickListener(view -> presenter.handleLoginClick(String.valueOf(editTextEmailUsername.getText()), String.valueOf(editTextPassword.getText())));
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void goToActivity(Class activity) {
        Intent i = new Intent(this, activity);
        startActivity(i);
    }
}