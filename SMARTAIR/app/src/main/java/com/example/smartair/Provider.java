package com.example.smartair;

import java.util.HashMap;

public class Provider extends User {
    HashMap<String, String> childrenAndCodes;
    public Provider() {
        super();
    }
    public Provider(String email, String password) {
        super(email, password);
        childrenAndCodes = new HashMap<>();
    }

    public HashMap<String, String> getChildrenAndCodes() {
        return childrenAndCodes;
    }

    public void setChildrenAndCodes(HashMap<String, String> childrenAndCodes) {
        this.childrenAndCodes = childrenAndCodes;
    }
}
