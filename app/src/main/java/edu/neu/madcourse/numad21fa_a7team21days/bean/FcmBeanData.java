package edu.neu.madcourse.numad21fa_a7team21days.bean;

public class FcmBeanData {

    private String title;
    private String body;
    private String receiveName;
    private int bigImage;
    private String sendName;

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

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public int getBigImage() {
        return bigImage;
    }

    public void setBigImage(int bigImage) {
        this.bigImage = bigImage;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    @Override
    public String toString() {
        return "FcmBeanData{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", receiveName='" + receiveName + '\'' +
                ", bigImage=" + bigImage +
                ", sendName='" + sendName + '\'' +
                '}';
    }
}
