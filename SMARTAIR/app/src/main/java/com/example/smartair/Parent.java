package com.example.smartair;
import java.util.ArrayList;

public class Parent extends User {
    ArrayList<String> linkedChildren;

    public Parent() {
        super();
    }
    public Parent(String email, String password) {
        super(email, password);
        linkedChildren = new ArrayList<>();
    }

    public ArrayList<String> getLinkedChildren() {
        return linkedChildren;
    }

    public void setLinkedChildren(ArrayList<String> linkedChildren) {
        this.linkedChildren = linkedChildren;
    }
}
