package edu.neu.madcourse.numad21fa_a7team21days.server;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import edu.neu.madcourse.numad21fa_a7team21days.R;
//import edu.neu.madcourse.numad21fa_a7team21days.activity.SendStickerActivity;
import edu.neu.madcourse.numad21fa_a7team21days.SendStickerActivity;
import edu.neu.madcourse.numad21fa_a7team21days.base.CCApplication;
import edu.neu.madcourse.numad21fa_a7team21days.bean.FcmBeanData;
import edu.neu.madcourse.numad21fa_a7team21days.utils.GsonConverter;
import edu.neu.madcourse.numad21fa_a7team21days.utils.SharedPrefUtils;

import static android.app.Notification.VISIBILITY_SECRET;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static volatile int NOTIFY_ID = 1;



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage == null) {
            return;
        }
        Map<String, String> remoteMessageMap = remoteMessage.getData();
        Log.i("aaaa", "MyFirebaseMessagingService 11 onMessageReceived  remoteMessageMap: " + remoteMessageMap);
        if (remoteMessageMap == null || remoteMessageMap.isEmpty()) {
            return;
        }
        JSONObject jsonObject = new JSONObject(remoteMessageMap);
        if (jsonObject == null) {
            return;
        }
        String dataJson = jsonObject.toString();
        Log.i("aaaa", "MyFirebaseMessagingService 11 onMessageReceived  dataJson: " + dataJson);
        if (true) {
            FcmBeanData fcmBeanData = GsonConverter.fromJson(dataJson, FcmBeanData.class);
            if (fcmBeanData == null) {
                return;
            }
            String currentUser = SharedPrefUtils.getInstance().getString("users");
            if (TextUtils.isEmpty(currentUser) || (!currentUser.equals(fcmBeanData.getReceiveName()))) {
                return;
            }
            final int notifyId = getNotificationID();
            Intent intent = new Intent(this, SendStickerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, notifyId /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Uri defaultSoundUri;
            String channelId;

            channelId = "a7team21days";
            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "numad21fa_a7team21days", NotificationManager.IMPORTANCE_HIGH);
                channel.setLockscreenVisibility(VISIBILITY_SECRET);
                channel.setSound(defaultSoundUri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
                channel.setBypassDnd(true);
                notificationManager.createNotificationChannel(channel);
            } else {
                notificationBuilder.setSound(defaultSoundUri);
            }

            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
            notificationBuilder.setContentTitle(fcmBeanData.getTitle());
            notificationBuilder.setContentText(fcmBeanData.getBody()).setAutoCancel(true);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
            if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);
            switch (fcmBeanData.getBigImage()) {
                case 1:
                    Bitmap bm1 = BitmapFactory.decodeResource(CCApplication.getInstance().getResources(), R.drawable.stciker_1);
                    notificationBuilder.setLargeIcon(bm1);
                    break;
                case 2:
                    Bitmap bm2 = BitmapFactory.decodeResource(CCApplication.getInstance().getResources(), R.drawable.sticker_2);
                    notificationBuilder.setLargeIcon(bm2);
                    break;
                case 3:
                    Bitmap bm3 = BitmapFactory.decodeResource(CCApplication.getInstance().getResources(), R.drawable.sticker_3);
                    notificationBuilder.setLargeIcon(bm3);
                    break;
                default:
                    Bitmap bm4 = BitmapFactory.decodeResource(CCApplication.getInstance().getResources(), R.mipmap.ic_launcher_round);
                    notificationBuilder.setLargeIcon(bm4);
                    break;
            }
            notificationManager.notify(notifyId /* ID of notification */, notificationBuilder.build());
        }
    }


    public int getNotificationID() {
        return ++NOTIFY_ID;
    }

}
