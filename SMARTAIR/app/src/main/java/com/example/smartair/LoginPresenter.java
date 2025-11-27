package com.example.smartair;

public class LoginPresenter {
    LoginModel model;
    LoginView view;

    public LoginPresenter(LoginModel model, LoginView view) {
        this.model = model;
        this.view = view;
    }

    public void determineScreen() {
        if (model.checkLoggedIn()) {
            view.goToActivity(OnboardingActivity.class);
        }
    }

    public void handleRoleSelectionClick() {
        view.goToActivity(RoleSelectionActivity.class);
    }

    public void handleLoginClick(String emailOrUsername, String password) {
        if(emailOrUsername.isEmpty()) {
            view.showMessage("Enter email or username");
            return;
        }

        if(password.isEmpty()) {
            view.showMessage("Enter password");
            return;
        }

        if (!(emailOrUsername.contains("@"))) {
            emailOrUsername += AddChildActivity.DOMAIN;
        }

        model.attemptSignIn(emailOrUsername, password, this);
    }

    public void handleLoginResult(boolean successful) {
        if (successful) {
            view.showMessage("Login successful!");
            view.goToActivity(OnboardingActivity.class);
        }
        else {
            view.showMessage("Authentication failed.");
        }
    }
}
