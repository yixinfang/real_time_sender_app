package edu.neu.madcourse.numad21fa_a7team21days;

import static android.content.ContentValues.TAG;

import static com.google.firebase.messaging.Constants.MessagePayloadKeys.SENDER_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);
        mDatabase = tools.mDatabase;
        mUser = mDatabase.child("users");
//        Map<String, User> users = new HashMap<>();
//        users = new HashMap<>();
//
////        users.put("yixin", new User("yixin"));
////        users.put("haowen", new User("haowen"));
//
//        mUser.setValue(users);

        // login user
        curUser = (String) getIntent().getSerializableExtra("userName");
        curUserTextView = findViewById(R.id.curUserText);
        curUserTextView.setText(curUser);

        receiver = findViewById(R.id.sentToUser);
        receiverName = receiver.getText().toString().trim();
        System.out.println(receiverName);
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            // check if the user exist in the database
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // check if "user" has a child of userGivenName
                if(snapshot.hasChild(receiverName)){
                    Toast.makeText(getApplicationContext(),"receiver exist",Toast.LENGTH_SHORT).show();
                    // if not create the user
                }else{
                    signUpUser(receiverName);
                    Toast.makeText(getApplicationContext(),"new receiver created",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        // 3 sticker to choose
        selectedImgTextView = findViewById(R.id.selectImgText);

        // TODO: get # of send
        getNumberOfSend();

        // selected  users exist in the database
        getDbUser();
        Spinner spinner = findViewById(R.id.userSpinner);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, usersOption);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0, false);
        for (String s : usersOption) {
            System.out.println(s);
        }

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // get the selected user
                System.out.println("1");
                selectedUser = parentView.getItemAtPosition(position).toString();
                Log.i("selectedUser", selectedUser);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e("something go wrong","123");
            }
        });
        adapter.notifyDataSetChanged();


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
//                FirebaseMessaging fm = FirebaseMessaging.getInstance();
//                Spinner spinner = findViewById(R.id.userSpinner);
//                fm.send(new RemoteMessage.Builder(SENDER_ID + spinner.getPopupContext())
//                        .addData("my_message", "Hello World")
//                        .addData("my_action","SAY_HELLO")
//                        .build());
                Spinner spinner = findViewById(R.id.userSpinner);
//                mUser = mDatabase.child("users").child(spinner.getSelectedItem().toString());

//                User cur = users.get("haowen");
//                Date date = Calendar.getInstance().getTime();
//                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//                String strDate = dateFormat.format(date);
//                cur.getReceivedSticker().add(new Sticker(selectedImgId, curUser, strDate));
//                mUser = mDatabase.child("users").child("haowen");
////                Map<String, User> users = new HashMap<>();
////                users.put("yixin", new User("Yixin"));
////                users.put("haowen", new User("Haowen"));
////                mUser.setValue(users);
                mUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Log.i("User Count " ,""+snapshot.getChildrenCount());
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            User user = postSnapshot.getValue(User.class);
                            Log.i("Get Data", user.getUserName());
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                            String curTime = LocalDate.now().format(formatter);

                            if(user.getUserName().equals(receiverName) && selectedImgId != null) {
                                user.getReceivedSticker().add(new Sticker(selectedImgId, curUser, curTime));
                                mDatabase.child("users").child(receiverName).setValue(user);
                                Toast.makeText(getApplicationContext(),"sticker sent",Toast.LENGTH_SHORT).show();
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











}
