package com.example.smartair;

public class HistoryItem {
    public static final String TYPE_ZONE = "zone";
    public static final String TYPE_INCIDENT = "incident";

    public long timestamp;
    public String type;
    public String detail;


    public HistoryItem() {}

    public HistoryItem(long timestamp, String type, String detail) {
        this.timestamp = timestamp;
        this.type = type;
        this.detail = detail;
    }
}
