package com.shimhg02.solorestorant.utils.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.ui.Activity.Main.MainActivity

class FCMService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle data payload of FCM messages.
        Log.d(
            TAG,
            "FCM Message Id: " + remoteMessage.messageId
        )
        Log.d(
            TAG,
            "FCM Notification Message: " + remoteMessage.notification
        )
        Log.d(TAG, "FCM Data Message: " + remoteMessage.data)
        val mPendingIntent = PendingIntent.getActivities(
            applicationContext,
            100,
            arrayOf(Intent(applicationContext, MainActivity::class.java)),
            PendingIntent.FLAG_ONE_SHOT
        )
        createNotification(
            remoteMessage.notification!!.title,
            remoteMessage.notification!!.body, mPendingIntent
        )
    }

    fun createNotification(
        title: String?,
        content: String?,
        pendingIntent: PendingIntent?
    ) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val soundUri = Uri.parse(
            "android.resource://"
                    + packageName + "/" + R.raw.alarm
        )
        val builder =
            NotificationCompat.Builder(applicationContext, "1")
        builder.setAutoCancel(true)
        builder.setSound(soundUri)
        builder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        builder.setContentTitle(title)
        builder.setContentText(content)
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent)
        }
        builder.setSmallIcon(R.mipmap.ic_launcher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.ic_launcher_foreground) //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            val channelName: CharSequence = "Nahonbab Channel"
            val description = "Channel for users"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("1", channelName, importance)
            channel.description = description
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(channel)
        } else builder.setSmallIcon(R.mipmap.ic_launcher) // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
        assert(notificationManager != null)
        notificationManager.notify(1234, builder.build()) // 고유숫자로 노티피케이션 동작시킴
    }

    companion object {
        private const val TAG = "MyFMService"
    }
}