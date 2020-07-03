package com.shimhg02.solorestorant.utils.Service;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shimhg02.solorestorant.R;
import com.shimhg02.solorestorant.ui.Activity.Main.MainActivity;


public class FCMService extends FirebaseMessagingService {

    private static final String TAG = "MyFMService";

    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());

        PendingIntent mPendingIntent = PendingIntent.getActivities(getApplicationContext(), 100,
                new Intent[]{new Intent(getApplicationContext(), MainActivity.class)}, PendingIntent.FLAG_ONE_SHOT);
        createNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(), mPendingIntent);
    }

    public void createNotification(String title, String content, @Nullable PendingIntent pendingIntent) {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Uri soundUri = Uri.parse("android.resource://"
                + getPackageName() + "/" + R.raw.alarm);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "1");
        builder.setAutoCancel(true);
        builder.setSound(soundUri);
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        builder.setContentTitle(title);
        builder.setContentText(content);
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }
        builder.setSmallIcon(R.mipmap.ic_launcher);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            CharSequence channelName  = "노티페케이션 채널";
            String description = "오레오 이상을 위한 것임";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("1", channelName , importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

        }else builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        assert notificationManager != null;
        notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작시킴
        }
}