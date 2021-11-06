package edu.neu.madcourse.numad21fa_a7team21days;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SendStickerActivity extends AppCompatActivity {

    private Integer numberOfSend;
    private String curUser;
    public ArrayList<String> usersOption = new ArrayList<>();

    private String selectedUser;
    private Integer selectedImgId = 1;

    private TextView curUserTextView;
    private TextView selectedImgTextView;
    private DatabaseReference mUser;
    private DatabaseReference mDatabase;
    private String FIREBASE_MESSAGE_TOKEN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);
        mDatabase = tools.mDatabase;
        mUser = mDatabase.child("users");
        Map<String, User> users = new HashMap<>();
        users.put("yixin", new User("Yixin"));
        users.put("haowen", new User("Haowen"));

        mUser.setValue(users);

        // login user
        curUser = (String) getIntent().getSerializableExtra("userName");
        curUserTextView = findViewById(R.id.curUserText);
        curUserTextView.setText(curUser);



        // 3 sticker to choose
        selectedImgTextView = findViewById(R.id.selectImgText);

        // TODO: get # of send
        getNumberOfSend();

        // selected  users exist in the database
        getDbUser();
        Spinner spinner = (Spinner) findViewById(R.id.userSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, usersOption);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // get the selected user
                selectedUser = parentView.getItemAtPosition(position).toString();
                Log.i("selectedUser", selectedUser);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e("something go wrong","123");
            }
        });


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
                        Log.i("FIREBASE_MESSAGE_TOKEN", FIREBASE_MESSAGE_TOKEN);
                    }
                });
    }

// FIREBASE_MESSAGE_TOKEN: ebk_vsvuShyGd1tTd3_2KJ:APA91bG0FdHkLtRMD1SUfOnCXbZ5iNz3kNtuU_Qvze2fT4lbDdAen7Qv3DcPSI-2AJhi8rDzLbbJk-R1U8BrihHB6akXBA1eqBhbsQn8zmT_jrrgBRQ5lxehcZBfsRw2zcgVTU0tzSLe

    public void getDbUser() {
        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersOption.clear();
                Log.i("User Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    Log.i("Get Data", user.getUserName());
                    if(!user.getUserName().equals(curUser)) {
                        usersOption.add(user.getUserName());
                    }
                }
                Log.i("user_List", usersOption.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Debug", "onCancelled: ");
            }
        });
    }


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

                break;
            case R.id.stickerImage2:
                // click fox
                selectedImgTextView.setText("fox");
                selectedImgId = 2;

                break;
            case R.id.stickerImage3:
                // click tutu
                selectedImgTextView.setText("rab");
                selectedImgId = 3;
                break;
            default:
                break;
        }
    }











}
