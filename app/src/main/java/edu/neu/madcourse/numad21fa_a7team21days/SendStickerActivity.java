package edu.neu.madcourse.numad21fa_a7team21days;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SendStickerActivity extends AppCompatActivity {
    private Integer numberOfSend;
    private String curUser;
    private String receivedUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);
        // 3 sticker to choose
    }
}