package edu.neu.madcourse.numad21fa_a7team21days;

import static android.content.ContentValues.TAG;

import static com.google.firebase.messaging.Constants.MessagePayloadKeys.SENDER_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendStickerActivity extends AppCompatActivity{

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
    private ArrayAdapter<String> adapter;
    private Map<String, User> users;
    private EditText receiver;
    private String receiverName;
    private Spinner mSpinner;
    private FirebaseMessaging firebaseMessaging;
    private MyAPIservice myAPIservice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);
        mDatabase = tools.mDatabase;
        mUser = mDatabase.child("users");
        myAPIservice = Client.getClient("https://fcm.googleapis.com/").create(MyAPIservice.class);


        // login user
        curUser = (String) getIntent().getSerializableExtra("userName");
        curUserTextView = findViewById(R.id.curUserText);
        curUserTextView.setText(curUser);

        receiver = findViewById(R.id.sentToUser);

        // 3 sticker to choose
        selectedImgTextView = findViewById(R.id.selectImgText);

        // TODO: get # of send
        getNumberOfSend();

        // selected  users exist in the database
        getDbUser();
        mSpinner = findViewById(R.id.userSpinner);


        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // get the selected user
                selectedUser = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e("something go wrong", "123");
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
                    }
                });


    }

// FIREBASE_MESSAGE_TOKEN: ebk_vsvuShyGd1tTd3_2KJ:APA91bG0FdHkLtRMD1SUfOnCXbZ5iNz3kNtuU_Qvze2fT4lbDdAen7Qv3DcPSI-2AJhi8rDzLbbJk-R1U8BrihHB6akXBA1eqBhbsQn8zmT_jrrgBRQ5lxehcZBfsRw2zcgVTU0tzSLe

    public void getDbUser() {
        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersOption.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    if(!user.getUserName().equals(curUser)) {
                        usersOption.add(user.getUserName());
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SendStickerActivity.this, android.R.layout.simple_spinner_item, usersOption);
                        mSpinner.setAdapter(adapter);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Debug", "onCancelled: ");
            }
        });
    }

    public void signUpUser(String userGivenName){
        // create new user
        User user = new User(userGivenName);
        mDatabase.child("users").child(userGivenName).setValue(user);
    }




    public void getNumberOfSend(){

    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.sendButton:
                // sent
                receiverName = receiver.getText().toString().trim();
                System.out.println(receiverName);
                mUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    // check if the user exist in the database
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // check if "user" has a child of userGivenName
                        if(snapshot.hasChild(selectedUser)){
                            Toast.makeText(getApplicationContext(),"receiver " + selectedUser +" exist",Toast.LENGTH_SHORT).show();
                            // if not create the user
                        }else{
                            signUpUser(selectedUser);
                            Toast.makeText(getApplicationContext(),"new receiver created",Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                mUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean validSend = false;

                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            User user = postSnapshot.getValue(User.class);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                            String curTime = LocalDate.now().format(formatter);

                            if(user.getUserName().equals(selectedUser) && selectedImgId != null) {
                                //user.getReceivedSticker().add(new Sticker(selectedImgId, curUser, curTime));
                                ////////////

                                ArrayList<Sticker> tmp = user.getReceivedSticker();
                                tmp.add(new Sticker(selectedImgId, curUser, curTime));
                                user.setReceivedSticker(tmp);
                                DatabaseReference ref = postSnapshot.getRef().child("receivedSticker");
                                //DatabaseReference ref = postSnapshot.getRef().child(receiverName).child("receivedSticker");
                                ref.setValue(tmp);

                                sendNotification(curUser, selectedUser, selectedImgId);
                                Toast.makeText(getApplicationContext(),"Notification sent",Toast.LENGTH_SHORT).show();

//                                mDatabase.child("users").child(receiverName).setValue(user);
                                Toast.makeText(getApplicationContext(),"sticker sent",Toast.LENGTH_SHORT).show();
                                RemoteMessage remoteMessage = new RemoteMessage.Builder(selectedUser)
                                        .setMessageId(Integer.toString(selectedImgId))
                                        .addData(curUser, curTime)
                                        .build();

                                validSend = true;
                            }
                        }
                        if (validSend) {
                            for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                User user = postSnapshot.getValue(User.class);
                                if(user.getUserName().equals(curUser)){
                                    DatabaseReference ref2 = postSnapshot.getRef().child("numberOfSend");
                                    ref2.setValue(user.getNumberOfSend()+1);
                                    Toast.makeText(getApplicationContext(),"you have sent " + user.getNumberOfSend()+1 +" stickers",Toast.LENGTH_SHORT).show();

                                }

                            }

                        }
//                        Log.i("user_receive_update", users.get);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Debug", "onCancelled: ");
                    }
                });
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

    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= String.valueOf(FirebaseMessaging.getInstance().getToken());
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }


    public void sendNotification(String curUser, String selectedUser, Integer selectedImgId) {
        Data data = new Data(curUser, selectedUser, selectedImgId);
        NotificationSender sender = new NotificationSender(data, selectedUser);
        Toast.makeText(getApplicationContext(),"Notification 1",Toast.LENGTH_SHORT).show();
        try {
            myAPIservice.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
                @Override
                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                    Log.i("send", response.message());
                    if (response.body() != null) {
                        Toast.makeText(getApplicationContext(),"Notification 2",Toast.LENGTH_SHORT).show();
                        if (response.body().success != 1) {
                            Toast.makeText(getApplicationContext(), "Failed ", Toast.LENGTH_LONG);
                        }
                        Toast.makeText(getApplicationContext(),"Notification 3",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MyResponse> call, Throwable t) {
                    Log.e("Error", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("send", e.getMessage());
        }

        Toast.makeText(getApplicationContext(),"Notification 4",Toast.LENGTH_SHORT).show();

    }











}
