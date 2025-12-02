package com.example.smartair;

public class Child extends User {
    String name;
    int age;
    int pb;
    int highQualitySessionNum;
    int lowRescueMonthNum;
    String greenActionPlan;
    String yellowActionPlan;
    String redActionPlan;
    String additionalNotes;
    String inviteCodeProvider;
    long providerCodeExpiry;
    boolean controllerToday;
    double rescueLeft;
    double controllerLeft;
    String rescuePurchaseDate;
    String rescueExpiryDate;
    String controllerPurchaseDate;
    String controllerExpiryDate;
    boolean inventoryMarkedLow;

    public Child() {
        super();
    }
    public Child(String username, String password) {
        super(username, password);
        name = "";
        age = 0;
        pb = 0;
        highQualitySessionNum = 0;
        lowRescueMonthNum = 0;
        greenActionPlan = "";
        yellowActionPlan = "";
        redActionPlan = "";
        additionalNotes = "";
        inviteCodeProvider = null;
        providerCodeExpiry = 0;
        controllerToday = true;
        rescueLeft = 100;
        controllerLeft = 100;
        rescuePurchaseDate = "";
        rescueExpiryDate = "";
        controllerPurchaseDate = "";
        controllerExpiryDate = "";
        inventoryMarkedLow = false;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getPb() {
        return pb;
    }

    public int getHighQualitySessionNum() {
        return highQualitySessionNum;
    }

    public int getLowRescueMonthNum() {
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

    public boolean getControllerToday() {
        return controllerToday;
    }

    public double getRescueLeft() {
        return rescueLeft;
    }

    public double getControllerLeft() {
        return controllerLeft;
    }

    public String getRescuePurchaseDate() {
        return rescuePurchaseDate;
    }

    public String getRescueExpiryDate() {
        return rescueExpiryDate;
    }

    public String getControllerPurchaseDate() {
        return controllerPurchaseDate;
    }

    public String getControllerExpiryDate() {
        return controllerExpiryDate;
    }

    public boolean getInventoryMarkedLow() {
        return inventoryMarkedLow;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPb(int pb) {
        this.pb = pb;
    }

    public void setHighQualitySessionNum(int highQualitySessionNum) {
        this.highQualitySessionNum = highQualitySessionNum;
    }

    public void setLowRescueMonthNum(int lowRescueMonthNum) {
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

    public void setControllerToday(boolean controllerToday) {
        this.controllerToday = controllerToday;
    }

    public void setRescueLeft(double rescueLeft) {
        this.rescueLeft = rescueLeft;
    }

    public void setControllerLeft(double controllerLeft) {
        this.controllerLeft = controllerLeft;
    }

    public void setRescuePurchaseDate(String rescuePurchaseDate) {
        this.rescuePurchaseDate = rescuePurchaseDate;
    }

    public void setRescueExpiryDate(String rescueExpiryDate) {
        this.rescueExpiryDate = rescueExpiryDate;
    }

    public void setControllerPurchaseDate(String controllerPurchaseDate) {
        this.controllerPurchaseDate = controllerPurchaseDate;
    }

    public void setControllerExpiryDate(String controllerExpiryDate) {
        this.controllerExpiryDate = controllerExpiryDate;
    }

    public void setInventoryMarkedLow(boolean inventoryMarkedLow) {
        this.inventoryMarkedLow = inventoryMarkedLow;
    }
}
