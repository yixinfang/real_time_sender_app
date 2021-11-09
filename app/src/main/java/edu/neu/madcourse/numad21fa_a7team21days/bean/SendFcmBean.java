package edu.neu.madcourse.numad21fa_a7team21days.bean;

public class SendFcmBean {
    /**
     * to : /topics/TopicName notification : {"title":"Breaking News","body":"New news story available."} data : www
     */

    private String to;
    /**
     * title : Breaking News body : New news story available.
     */

    private NotificationBean notification;
    private FcmBeanData data;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NotificationBean getNotification() {
        return notification;
    }

    public void setNotification(NotificationBean notification) {
        this.notification = notification;
    }

    public FcmBeanData getData() {
        return data;
    }

    public void setData(FcmBeanData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SendFcmBean{" +
                "to='" + to + '\'' +
                ", notification=" + notification +
                ", data='" + data + '\'' +
                '}';
    }
}
