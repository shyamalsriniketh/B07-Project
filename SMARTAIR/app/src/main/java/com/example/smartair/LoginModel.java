package com.example.smartair;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginModel {
    FirebaseAuth mAuth;

    public LoginModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean checkLoggedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    public void attemptSignIn(String email, String password, LoginPresenter presenter) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            presenter.handleLoginResult(task.isSuccessful());
        });
    }
}
