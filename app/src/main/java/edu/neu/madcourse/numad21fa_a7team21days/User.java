package edu.neu.madcourse.numad21fa_a7team21days;

import java.util.ArrayList;

public class User {
    private String userName;
    private Integer numberOfSend;
    private ArrayList receivedSticker;

    public User(String userName) {
        this.userName = userName;
        this.numberOfSend = 0;
        this.receivedSticker = new ArrayList<String>();
        this.receivedSticker.add("init");
    }

    public String getUserName() {
        return userName;
    }

    public Integer getNumberOfSend() {
        return numberOfSend;
    }

    public ArrayList<String> getReceivedSticker() {
        return receivedSticker;
    }
}
