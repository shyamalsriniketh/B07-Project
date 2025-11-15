package com.example.smartair;

public abstract class User {
    String id;
    boolean onboarded= false;
    String password;
    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
