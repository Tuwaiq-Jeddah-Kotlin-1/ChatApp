package com.myprojects.chatapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val CHANNEL_ID = "notification_channel"
const val CHANNEL_NAME = "com.myprojects.chatapp"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    fun generateNotification(senderUserName: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
            CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_account_circle_24)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(senderUserName))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())
    }

    private fun getRemoteView(senderUserName: String): RemoteViews {
        val remoteView = RemoteViews("com.myprojects.chatapp",R.layout.notification)
        remoteView.setTextViewText(R.id.notificationSenderTV,senderUserName)
        return remoteView
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null){
            generateNotification(remoteMessage.notification!!.title!!)
        }
    }
}