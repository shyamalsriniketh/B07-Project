package com.example.smartair;
import java.util.ArrayList;

public class Parent extends User {
    ArrayList<Child> linkedChildren;

    public Parent() {
        super();
    }
    public Parent(String email, String password) {
        super(email, password);
        linkedChildren = new ArrayList<>();
    }

    public ArrayList<Child> getLinkedChildren() {
        return linkedChildren;
    }

    public void setLinkedChildren(ArrayList<Child> linkedChildren) {
        this.linkedChildren = linkedChildren;
    }
}
