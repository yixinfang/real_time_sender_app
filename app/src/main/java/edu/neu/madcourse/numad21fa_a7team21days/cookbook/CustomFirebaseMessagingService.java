package edu.neu.madcourse.numad21fa_a7team21days.cookbook;

import static android.app.Notification.VISIBILITY_SECRET;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import edu.neu.madcourse.numad21fa_a7team21days.R;
import edu.neu.madcourse.numad21fa_a7team21days.SendStickerActivity;
import edu.neu.madcourse.numad21fa_a7team21days.cookbook.User;
import edu.neu.madcourse.numad21fa_a7team21days.cookbook.tools;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

    private static volatile int NOTIFY_ID = 1;
    private String sender;
    private String receiver;
    private String imageID;
    private String title;
    private String message;

    private static final String TAG = "mFirebaseIIDService";
    private static final String SUBSCRIBE_TO = "userABC";
    private String token;

    public static class companion{
        SharedPreferences sharedPreferences;
        String token;

        public companion(SharedPreferences sharedPreferences, String token) {
            this.sharedPreferences = sharedPreferences;
            this.token = token;
        }

        public SharedPreferences getSharedPreferences() {
            return sharedPreferences;
        }

        public void setSharedPreferences(SharedPreferences sharedPreferences) {
            this.sharedPreferences = sharedPreferences;
        }

        public String getToken() {
            return sharedPreferences.getString("token", "");
        }

        public void setToken(String value) {
            sharedPreferences.edit().putString("token", value).apply();
        }
    }




    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);
        token = newToken;

        /*
          This method is invoked whenever the token refreshes
          OPTIONAL: If you want to send messages to this application instance
          or manage this apps subscriptions on the server side,
          you can send this token to your server.
        */
        String refreshToken = FirebaseMessaging.getInstance().getToken().toString();

        // Once the token is generated, subscribe to topic with the userId
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
        // to save token in firebase db
        User user = new User(token);
        tools.mDatabase.child("users").child(token).setValue(user);
        Log.i(TAG, "onTokenRefresh completed with token: " + refreshToken);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage); ////
        Toast.makeText(getApplicationContext(),"Notification received",Toast.LENGTH_SHORT).show();

        String channelId = "numad21fa_a7team";
        String channelName = "NUMAD21FA";
        sender = remoteMessage.getData().get("sender");
        receiver = remoteMessage.getData().get("receiver");
        imageID = remoteMessage.getData().get("imageID");
        title = "Hi, " + receiver;
        message = "You have received a sticker from " + sender;


        //curUser = (String) getIntent().getSerializableExtra("userName");
        if (remoteMessage == null) {
            return;
        }
        String notificationTitle = remoteMessage.getNotification().getTitle();
        String notificationContent = remoteMessage.getNotification().getBody();

        Intent intent = new Intent(this, SendStickerActivity.class); ////
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);////

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);////
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);////
        String notificationID = receiver;////
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            //String channelId  = getString(R.string.default_notification_channel_id);
            //String channelName = getString(R.string.default_notification_channel_name);
            NotificationChannel notificationChannel =
                    new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setLockscreenVisibility(VISIBILITY_SECRET);
            notificationChannel.setSound(defaultSoundUri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
            notificationChannel.setBypassDnd(true);
            notificationManager.createNotificationChannel(notificationChannel);
        } else {
            notificationBuilder.setSound(defaultSoundUri);
        }
        Toast.makeText(getApplicationContext(),"Notification 5",Toast.LENGTH_SHORT).show();

        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        switch (Integer.valueOf(imageID)) {
            case 1:
                Bitmap bm1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.stciker_1);
                notificationBuilder.setLargeIcon(bm1);
                break;
            case 2:
                Bitmap bm2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.sticker_2);
                notificationBuilder.setLargeIcon(bm2);
                break;
            case 3:
                Bitmap bm3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.sticker_3);
                notificationBuilder.setLargeIcon(bm3);
                break;
            default:
                Bitmap bm4 = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher_round);
                notificationBuilder.setLargeIcon(bm4);
                break;
        }
        notificationBuilder.build();
        notificationManager.notify();

        //createNotification(notificationTitle, notificationContent);
//        RemoteMessage remoteMessage1 = new RemoteMessage()

    }

//    private void createNotification(String notificationTitle, String notificationContent) {
//        NotificationCompat.Builder builder = new
//                NotificationCompat.Builder(this,
//                getResources().getString(R.string.default_notification_channel_id));
//
//    }

}
