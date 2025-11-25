package com.example.smartair;

public class Child extends User {
    String name;
    String age;
    String pb;
    String highQualitySessionNum;
    String lowRescueMonthNum;
    String greenActionPlan;
    String yellowActionPlan;
    String redActionPlan;
    String additionalNotes;
    String inviteCodeProvider;
    long providerCodeExpiry;
    boolean dataSharedWithProvider;

    public Child() {
        super();
    }
    public Child(String username, String password) {
        super(username, password);
        name = "";
        age = "";
        pb = "";
        highQualitySessionNum = "";
        lowRescueMonthNum = "";
        greenActionPlan = "";
        yellowActionPlan = "";
        redActionPlan = "";
        additionalNotes = "";
        inviteCodeProvider = null;
        providerCodeExpiry = 0;
        dataSharedWithProvider = false;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getPb() {
        return pb;
    }

    public String getHighQualitySessionNum() {
        return highQualitySessionNum;
    }

    public String getLowRescueMonthNum() {
        return lowRescueMonthNum;
    }

    public String getGreenActionPlan() {
        return greenActionPlan;
    }

    public String getYellowActionPlan() {
        return yellowActionPlan;
    }

    public String getRedActionPlan() {
        return redActionPlan;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public String getInviteCodeProvider() {
        return inviteCodeProvider;
    }

    public long getProviderCodeExpiry() {
        return providerCodeExpiry;
    }

    public boolean getDataSharedWithProvider() {
        return dataSharedWithProvider;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setPb(String pb) {
        this.pb = pb;
    }

    public void setHighQualitySessionNum(String highQualitySessionNum) {
        this.highQualitySessionNum = highQualitySessionNum;
    }

    public void setLowRescueMonthNum(String lowRescueMonthNum) {
        this.lowRescueMonthNum = lowRescueMonthNum;
    }

    public void setGreenActionPlan(String greenActionPlan) {
        this.greenActionPlan = greenActionPlan;
    }

    public void setYellowActionPlan(String yellowActionPlan) {
        this.yellowActionPlan = yellowActionPlan;
    }

    public void setRedActionPlan(String redActionPlan) {
        this.redActionPlan = redActionPlan;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public void setInviteCodeProvider(String inviteCodeProvider) {
        this.inviteCodeProvider = inviteCodeProvider;
    }

    public void setProviderCodeExpiry(long providerCodeExpiry) {
        this.providerCodeExpiry = providerCodeExpiry;
    }

    public void setDataSharedWithProvider(boolean dataSharedWithProvider) {
        this.dataSharedWithProvider = dataSharedWithProvider;
    }

}
