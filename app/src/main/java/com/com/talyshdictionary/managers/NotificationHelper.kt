package com.com.talyshdictionary.managers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.com.talyshdictionary.R
import com.com.talyshdictionary.activities.MainActivity

class NotificationHelper internal constructor(private val cont : Context){

     companion object {
         private  val NOTIFICATION_CHANNEL_ID = "10001"
     }

    fun aCreateNotify() {
    val intent =  Intent(cont, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    val resultPendingIntent = PendingIntent.getActivity(cont, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

         val builder = NotificationCompat.Builder(cont, cont.getString(R.string.app_name))

    // TODO: Step 1.8 use the new 'breakfast' notification channel

    // TODO: Step 1.3 set title, text and icon to builder
        .setSmallIcon(R.drawable.talysh_front)
        .setContentTitle(cont
        .getString(R.string.notification_title))
        .setContentText("test body messages ")

    val mNotificationManager =  cont.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
    {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(false)

        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        mNotificationManager.createNotificationChannel(notificationChannel)
    }

    mNotificationManager.notify(0 /* Request Code */, builder.build())
       }
     }