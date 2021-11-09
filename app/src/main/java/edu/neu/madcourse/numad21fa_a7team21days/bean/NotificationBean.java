package edu.neu.madcourse.numad21fa_a7team21days.bean;

public class NotificationBean {

    private String title;
    private String body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NotificationBean{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
