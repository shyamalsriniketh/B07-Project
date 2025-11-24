package com.example.smartair;

public abstract class User {
    String id;
    boolean onboarded;
    String password;

    public User() {}

    public User(String id, String password) {
        this.id = id;
        this.password = password;
        onboarded = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getOnboarded() {
        return onboarded;
    }

    public void setOnboarded(boolean onboarded) {
        this.onboarded = onboarded;
    }
}