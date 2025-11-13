package com.example.myapplication;

public abstract class User {
    String id;
    boolean onboarded;
    String password;
    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
