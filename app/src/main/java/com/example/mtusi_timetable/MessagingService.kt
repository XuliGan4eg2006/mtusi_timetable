package com.example.mtusi_timetable

import android.Manifest
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService: FirebaseMessagingService() {
    val TAG = "[Firebase] MessagingService"

    override fun onNewToken(token: String) {
        Log.d(TAG, token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, message.toString())
        if (message.notification != null) {
            //send notification
            sendMessage(message)
        }
    }

    private fun sendMessage(message: RemoteMessage) {
        val channelId = "default_channel"
        val notificationId = 1
        val notificationManager = NotificationManagerCompat.from(this)
        val createNotification = NotificationCompat.Builder(this, channelId)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["body"])
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(notificationId, createNotification)
    }

}