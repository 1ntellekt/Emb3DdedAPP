package com.example.emb3ddedapp.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.emb3ddedapp.R
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random


class FireServices:FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remMesg: RemoteMessage) {
        super.onMessageReceived(remMesg)
        //Log.i("tagPush", "onMessageReceive() title| ${remMesg.notification?.title}")
        //Log.i("tagPush", "onMessageReceive() body| ${remMesg.notification?.body}")
        remMesg.notification?.let {
            if (it.title!=null && it.body != null){
                if (it.title != "action" )
                showNotification(it.title!!, it.body!!)
            }
        }
        val intent = Intent(PUSH_TAG)
        remMesg.data.forEach{ entry ->
            Log.i("tagPush", "${entry.key}|${entry.value}")
            intent.putExtra(entry.key, entry.value)
        }

        sendBroadcast(intent)

        Log.i("tagPush", "onMessageReceive() data | ${remMesg.data}")
    }


    companion object {
        const val NAME_CHANNEL="idFbChannel"
        const val CHANNEL_ID = "123"
        const val PUSH_TAG = "push_tag"
        const val KEY_ACTION="action"
        const val ACTION_SHOW_MESSAGE="show_message"
        const val ACTION_ORDER = "order"
    }

    private fun showNotification(title:String, body:String){
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        val notId = Random.nextInt()

        //intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManagerCompat)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)

        notification.setStyle(
            NotificationCompat.BigTextStyle()
            .setBigContentTitle(title)
            .setSummaryText("Body").bigText(body))
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManagerCompat.notify(notId,notification.build())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManagerCompat: NotificationManagerCompat){
        val channel = NotificationChannel(CHANNEL_ID,NAME_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManagerCompat.createNotificationChannel(channel)
    }


}