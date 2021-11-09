package edu.neu.madcourse.numad21fa_a7team21days;

public class NotificationSender {
    private Data data;
    private String to;

    public NotificationSender(Data data, String to) {
        this.data = data;
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
