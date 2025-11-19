package com.example.smartair;
import java.util.ArrayList;

public class Parent extends User {
    ArrayList<Child> linkedChildren;

    public Parent() {
        super();
    }
    public Parent(String email, String password) {
        super(email, password);
    }
    public void setLinkedChildren(ArrayList<Child> linkedChildren) {
        this.linkedChildren = linkedChildren;
    }
}
