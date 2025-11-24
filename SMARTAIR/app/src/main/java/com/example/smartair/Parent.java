package com.example.smartair;
import java.util.ArrayList;

public class Parent extends User {
    ArrayList<Child> linkedChildren;
    String invitecodeProvider;
    long ProviderCodeExpiry;
    boolean DataSharedWithProvider;

    public Parent() {
        super();
    }
    public Parent(String email, String password) {
        super(email, password);
        linkedChildren = new ArrayList<>();
        invitecodeProvider = null;
        DataSharedWithProvider = false;
    }

    public ArrayList<Child> getLinkedChildren() {
        return linkedChildren;
    }

    public void setLinkedChildren(ArrayList<Child> linkedChildren) {
        this.linkedChildren = linkedChildren;
    }
    public void updateInvite(String code, long expiry){
        this.invitecodeProvider= code;
        this.ProviderCodeExpiry= expiry;
    }

}
