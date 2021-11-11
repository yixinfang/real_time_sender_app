package edu.neu.madcourse.numad21fa_a7team21days;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import edu.neu.madcourse.numad21fa_a7team21days.bean.FcmBeanData;
import edu.neu.madcourse.numad21fa_a7team21days.bean.NotificationBean;
import edu.neu.madcourse.numad21fa_a7team21days.bean.SendFcmBean;
import edu.neu.madcourse.numad21fa_a7team21days.bean.SendFcmResponse;
import edu.neu.madcourse.numad21fa_a7team21days.cookbook.Sticker;
import edu.neu.madcourse.numad21fa_a7team21days.cookbook.Data;
import edu.neu.madcourse.numad21fa_a7team21days.cookbook.MyAPIservice;
import edu.neu.madcourse.numad21fa_a7team21days.cookbook.MyResponse;
import edu.neu.madcourse.numad21fa_a7team21days.cookbook.NotificationSender;
import edu.neu.madcourse.numad21fa_a7team21days.cookbook.Token;
import edu.neu.madcourse.numad21fa_a7team21days.cookbook.User;
import edu.neu.madcourse.numad21fa_a7team21days.cookbook.tools;
import edu.neu.madcourse.numad21fa_a7team21days.http.ApiEndpointClient;
import edu.neu.madcourse.numad21fa_a7team21days.utils.SharedPrefUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import com.android.volley.Response;


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
    private TextView receiver;
    private String receiverName;
    private Spinner mSpinner;
    private FirebaseMessaging firebaseMessaging;
    private MyAPIservice myAPIservice;
    private Button sendBtn;
    private String topic = "myTopic";
    private String firebase_message_token;
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = {MESSAGING_SCOPE};
    private String toDaMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sticker);
        mDatabase = tools.mDatabase;
        mUser = mDatabase.child("users");
        FirebaseMessaging.getInstance().subscribeToTopic("TopicName");

        // login user
        curUser = (String) getIntent().getSerializableExtra("userName");
        curUserTextView = findViewById(R.id.curUserText);
        String curUserMessage = "Hi " + curUser;
        curUserTextView.setText(curUserMessage);

        SharedPrefUtils.getInstance().putString("users", curUser);


        receiver = findViewById(R.id.sentToUser);

        // 3 sticker to choose
        selectedImgTextView = findViewById(R.id.selectImgText);


        // selected  users exist in the database
        getDbUser();
        mSpinner = findViewById(R.id.userSpinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // get the selected user
                selectedUser = parentView.getItemAtPosition(position).toString();
                Log.e("selectedUser", selectedUser);
                toDaMessage = selectedUser + "will receive a sticker: ";
               // receiver.setText(toDaMessage);
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
                        FIREBASE_MESSAGE_TOKEN = task.getResult();
                        // Log and toast
                        Log.e("FIREBASE_MESSAGE_TOKEN", FIREBASE_MESSAGE_TOKEN);
                    }
                });

    }


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



    public void onClick(View view){
        switch (view.getId()) {
            case R.id.sendButton:
                // sent
                doUpdate();
                doSendNotification();
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

    private void doUpdate() {
        if (selectedUser != null) {
            Toast.makeText(getApplicationContext(),"receiver " + selectedUser +" exist",Toast.LENGTH_SHORT).show();
        }
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean validSend = false;

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);

                    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss");

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                    String curTime = LocalDate.now().format(formatter);
                    //String curTime = "20211108";

                    if(user.getUserName().equals(selectedUser) && selectedImgId != null) {
                        //user.getReceivedSticker().add(new Sticker(selectedImgId, curUser, curTime));
                        ////////////

                        ArrayList<Sticker> tmp = user.getReceivedSticker();
                        tmp.add(new Sticker(selectedImgId, curUser, curTime));
                        user.setReceivedSticker(tmp);
                        DatabaseReference ref = postSnapshot.getRef().child("receivedSticker");
                        //DatabaseReference ref = postSnapshot.getRef().child(receiverName).child("receivedSticker");
                        ref.setValue(tmp);


//                                sendNotification(curUser, selectedUser, selectedImgId);
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
                            // revised by haowen
                            Integer pastSend = (user.getNumberOfSend() + 1);
                            Toast.makeText(getApplicationContext(),"you have sent " + pastSend +" stickers",Toast.LENGTH_SHORT).show();

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
    }


    private void doSendNotification() {
        SendFcmBean sendFcmBean = new SendFcmBean();
        sendFcmBean.setTo("/topics/TopicName");
        NotificationBean notificationBean = new NotificationBean();
        notificationBean.setTitle("Hi i am title");
        notificationBean.setTitle("Hi i am body");
        sendFcmBean.setNotification(notificationBean);

        FcmBeanData fcmBeanData = new FcmBeanData();
        String titleCur = "Hi "+ selectedUser;
        fcmBeanData.setTitle(titleCur);
        String messageCur = "You've received a sticker from " + curUser;
        fcmBeanData.setBody(messageCur);
        fcmBeanData.setSendName(curUser);
        fcmBeanData.setReceiveName(selectedUser);
        fcmBeanData.setBigImage(selectedImgId);
        sendFcmBean.setData(fcmBeanData);

        ApiEndpointClient.getEndpointV2().sendFCMMessage(sendFcmBean)
                .enqueue(new Callback<SendFcmResponse>() {
                    @Override
                    public void onResponse(Call<SendFcmResponse> call, Response<SendFcmResponse> response) {
                        Log.i("aaa", "sendFCMMessage response : " + response.body());

                        SendFcmResponse sendFcmResponse = response.body();
                        if (sendFcmResponse != null && sendFcmResponse.getMessage_id() > 0) {
                            ToastUtils.showShort("send success message_id : " + sendFcmResponse.getMessage_id());
                        }
                    }

                    @Override
                    public void onFailure(Call<SendFcmResponse> call, Throwable t) {

                    }
                });


    };

//    public void sentNotification() {
//
//        String NOTIFICATION_TAG = "NOTIFICATION TAG";
//        String title = "Hi, " + selectedUser;
//        String message = "You've received a sticker from " + curUser;
//        JSONObject notification = new JSONObject();
//        JSONObject notificationBody = new JSONObject();
//        try {
//            notificationBody.put("title", title);
//            notificationBody.put("message", message);
//            notification.put("imageID", selectedImgId);
//        } catch (JSONException e) {
//            Log.e(NOTIFICATION_TAG, "onCreate: " + e.getMessage() );
//        }
//        doNoti(notification);
//    }

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAabFiQns:APA91bG3ArtLRHlcn2mZdYl-02AM-mvUISQlJVIJx2jUhaKlKZmYw2Hc2RptWeOys8QJJreX5m9eWCkZKpxtzEO2YsrMnU4UC0es-MWdsotHg8Nl5lE1qrtapNQjh6MJrXtIZlk8oOB3";
    final private String contentType = "application/json";

//    public void doNoti(JSONObject notification) {
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i(TAG, "onResponse: " + response.toString());
////                        edtTitle.setText("");
////                        edtMessage.setText("");
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(SendStickerActivity.this, "Request error", Toast.LENGTH_LONG).show();
//                        Log.i(TAG, "onErrorResponse: Didn't work");
//                    }
//                }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Authorization", serverKey);
//                params.put("Content-Type", contentType);
//                return params;
//            }
//        };
//        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
//
//    }

    private void doSentNotification() {
        SendFcmBean sendFcmBean = new SendFcmBean();
        sendFcmBean.setTo("/topics/TopicName");
        NotificationBean notificationBean = new NotificationBean();
        notificationBean.setTitle("Hi i am title");
        notificationBean.setTitle("Hi i am body");
        sendFcmBean.setNotification(notificationBean);

        FcmBeanData fcmBeanData = new FcmBeanData();
        fcmBeanData.setTitle("Hi i am title");
        fcmBeanData.setBody("Hi i am body");
        fcmBeanData.setSendName(curUser);
        fcmBeanData.setReceiveName(selectedUser);
        fcmBeanData.setBigImage(selectedImgId);
        sendFcmBean.setData(fcmBeanData);

        ApiEndpointClient.getEndpointV2().sendFCMMessage(sendFcmBean)
                .enqueue(new Callback<SendFcmResponse>() {
                    @Override
                    public void onResponse(Call<SendFcmResponse> call, Response<SendFcmResponse> response) {
                        Log.i("aaa", "sendFCMMessage response : " + response.body());

                        SendFcmResponse sendFcmResponse = response.body();
                        if (sendFcmResponse != null && sendFcmResponse.getMessage_id() > 0) {
                            ToastUtils.showShort("send success message_id : " + sendFcmResponse.getMessage_id());
                        }
                    }

                    @Override
                    public void onFailure(Call<SendFcmResponse> call, Throwable t) {

                    }
                });
    }

    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= String.valueOf(FirebaseMessaging.getInstance().getToken());
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }


    public void sendNotification() {

        Data data = new Data(curUser, selectedUser, selectedImgId);
        NotificationSender sender = new NotificationSender(data, selectedUser);
        Toast.makeText(getApplicationContext(),"Notification 1",Toast.LENGTH_SHORT).show();
        try {
            myAPIservice.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
                @Override
                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                    Log.i("send", response.message());
                    if (response.isSuccessful()) {
                        Log.i("send_tag", response.toString());
                        Toast.makeText(getApplicationContext(),"Notification 2",Toast.LENGTH_SHORT).show();
//                        if (response.body().success != 1) {
//                            Toast.makeText(getApplicationContext(), "Failed ", Toast.LENGTH_LONG);
//                        }
//                        Toast.makeText(getApplicationContext(),"Notification 3",Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("send_tag_err", response.errorBody().toString());
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
