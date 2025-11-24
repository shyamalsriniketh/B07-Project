package com.example.smartair;

public class Parent extends User {
    String invitecodeProvider= null;
    long ProviderCodeExpiry;
    boolean DataSharedWithProvider= false;
    public Parent() {
        super();
    }
    public Parent(String email, String password) {
        super(email, password);
    }
    public void updateInvite(String code, long expiry){
        this.invitecodeProvider= code;
        this.ProviderCodeExpiry= expiry;
    }

}
