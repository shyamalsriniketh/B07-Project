package com.example.smartair;

public class Child extends User {
    String name;
    String dob;

    public Child() {
        super();
    }
    public Child(String username, String password) {
        super(username, password);
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }
}
