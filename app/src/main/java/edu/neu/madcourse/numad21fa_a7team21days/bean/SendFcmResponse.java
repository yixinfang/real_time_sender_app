package edu.neu.madcourse.numad21fa_a7team21days.bean;

public class SendFcmResponse {

    /**
     * message_id : 7061423544042760116
     */

    private long message_id;

    public long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(long message_id) {
        this.message_id = message_id;
    }

    @Override
    public String toString() {
        return "SendFcmResponse{" +
                "message_id=" + message_id +
                '}';
    }
}
