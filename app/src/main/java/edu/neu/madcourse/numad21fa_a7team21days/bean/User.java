//package edu.neu.madcourse.numad21fa_a7team21days.bean;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//
//public class User {
//    private String userName;
//    private Integer numberOfSend;
//    private ArrayList<Sticker> receivedSticker;
//
//    public User(String userName) {
//        this.userName = userName;
//        this.numberOfSend = 0;
//        this.receivedSticker = new ArrayList<Sticker>();
//
//        // testing for later usage
//        Date date = Calendar.getInstance().getTime();
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//        String strDate = dateFormat.format(date);
//        this.receivedSticker.add(new Sticker(1,userName,strDate));
//    }
//
//    public User(){ }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public Integer getNumberOfSend() {
//        return numberOfSend;
//    }
//
//    public ArrayList<Sticker> getReceivedSticker() {
//        return receivedSticker;
//    }
//
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public void setNumberOfSend(Integer numberOfSend) {
//        this.numberOfSend = numberOfSend;
//    }
//
//    public void setReceivedSticker(ArrayList<Sticker> receivedSticker) {
//        this.receivedSticker = receivedSticker;
//    }
//}
