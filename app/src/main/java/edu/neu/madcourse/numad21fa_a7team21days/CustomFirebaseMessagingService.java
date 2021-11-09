package edu.neu.madcourse.numad21fa_a7team21days;

import static android.app.Notification.VISIBILITY_SECRET;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

    private static volatile int NOTIFY_ID = 1;
    private String sender;
    private String receiver;
    private String imageID;
    private String title;
    private String message;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
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

        Intent intent = new Intent(this, SendStickerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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

        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message).setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
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
