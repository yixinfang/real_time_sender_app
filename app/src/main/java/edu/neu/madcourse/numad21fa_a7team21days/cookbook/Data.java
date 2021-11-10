package edu.neu.madcourse.numad21fa_a7team21days.cookbook;

public class Data {
    private String sender;
    private String receiver;
    private Integer imageID;

    public Data(String sender, String receiver, Integer imageID) {
        this.sender = sender;
        this.receiver = receiver;
        this.imageID = imageID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }
}
