package edu.neu.madcourse.numad21fa_a7team21days;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SendStickerActivity extends AppCompatActivity {

    private Integer numberOfSend;
    private String receivedUser;
    private String curUser;

    private TextView curUserTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);


        // login user
        curUser = (String) getIntent().getSerializableExtra("userName");

        curUserTextView = findViewById(R.id.curUserText);
        curUserTextView.setText(curUser);

        // 3 sticker to choose


    }
}