package com.example.smartair;

public class IncidentLog {

    public String category;        // "Emergency", "Yellow", "Green"
    public boolean cantSpeak;
    public boolean chestPulling;
    public boolean blueLips;
    public int rescueAttempts;
    public int pef;
    public String stepsShown;
    public String ChildUid;

    public IncidentLog() {} // required

    public IncidentLog( String c,
                       boolean cs, boolean ch, boolean bl,
                       int resc, int pefVal, String plan,String Uid) {

        category = c;
        cantSpeak = cs;
        chestPulling = ch;
        blueLips = bl;
        rescueAttempts = resc;
        pef = pefVal;
        stepsShown = plan;
        ChildUid = Uid;
    }
}

