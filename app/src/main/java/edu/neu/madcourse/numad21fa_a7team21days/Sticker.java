package edu.neu.madcourse.numad21fa_a7team21days;

import java.time.LocalDateTime;

public class Sticker {
    private String stickerId;
    private String sender;
    private LocalDateTime sendTime;

    public Sticker(String stickerId, String sender, LocalDateTime sendTime) {
        this.stickerId = stickerId;
        this.sender = sender;
        this.sendTime = sendTime;
    }

    public String getStickerId() {
        return stickerId;
    }

    public String getSender() {
        return sender;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }
}
