package edu.neu.madcourse.numad21fa_a7team21days;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class SendStickerActivity extends AppCompatActivity {

    private Integer numberOfSend;
    private String receivedUser;
    private String curUser;
    private Integer selectedImgId;


    private TextView curUserTextView;
    private TextView selectedImgTextView;
    private String FIREBASE_MESSAGE_TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);

        // login user
        curUser = (String) getIntent().getSerializableExtra("userName");
        curUserTextView = findViewById(R.id.curUserText);
        curUserTextView.setText(curUser);


        // 3 sticker to choose
        selectedImgTextView = findViewById(R.id.selectImgText);

        // get # of send
        getNumberOfSend();

        // get token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token

                        String FIREBASE_MESSAGE_TOKEN = task.getResult();

                        // Log and toast
                        Log.e("FIREBASE_MESSAGE_TOKEN", FIREBASE_MESSAGE_TOKEN);
                    }
                });
    }

// FIREBASE_MESSAGE_TOKEN: ebk_vsvuShyGd1tTd3_2KJ:APA91bG0FdHkLtRMD1SUfOnCXbZ5iNz3kNtuU_Qvze2fT4lbDdAen7Qv3DcPSI-2AJhi8rDzLbbJk-R1U8BrihHB6akXBA1eqBhbsQn8zmT_jrrgBRQ5lxehcZBfsRw2zcgVTU0tzSLe



    public void getNumberOfSend(){

    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.sendButton:
                // sent
                break;
            case R.id.historyButton:
                // show history
                Intent intent = new Intent(this, HistoryActivity.class);
                intent.putExtra("userName", curUser);
                startActivity(intent);
                break;
            case R.id.stickerImage1:
                // click maomao
                selectedImgTextView.setText("cat");
                selectedImgId = 1;
//                Toast.makeText(getApplicationContext(),"cat press",Toast.LENGTH_SHORT).show();
                break;
            case R.id.stickerImage2:
                // click fox
                selectedImgTextView.setText("fox");
                selectedImgId = 2;
//                Toast.makeText(getApplicationContext(),"fox press",Toast.LENGTH_SHORT).show();
                break;
            case R.id.stickerImage3:
                // click tutu
                selectedImgTextView.setText("rab");
                selectedImgId = 3;
//                Toast.makeText(getApplicationContext(),"rabbit press",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }











}
