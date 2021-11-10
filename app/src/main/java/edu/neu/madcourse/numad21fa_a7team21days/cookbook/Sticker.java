package edu.neu.madcourse.numad21fa_a7team21days.cookbook;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Sticker {
    private Integer stickerId;
    private String sender;
    private String sendTime;

    public Sticker(Integer stickerId, String sender, String sendTime) {
        this.stickerId = stickerId;
        this.sender = sender;
        this.sendTime = sendTime;
    }

    public Sticker(){ }

    public Integer getStickerId() {
        return stickerId;
    }

    public String getSender() {
        return sender;
    }

    public String getSendTime() {
        return sendTime;
    }
}
