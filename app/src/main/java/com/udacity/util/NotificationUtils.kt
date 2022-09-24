

package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R

private val NOTIFICATION_ID = 0
fun NotificationManager.sendNotification(status:String ,fileName:String,applicationContext: Context) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra("status",status)
    contentIntent.putExtra("fileName",fileName)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


        val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.channel_id)
        ).setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(applicationContext.getString(R.string.notification_description))
            .setContentIntent(contentPendingIntent)
            .addAction(
                R.drawable.ic_assistant_black_24dp,
                applicationContext.getString(R.string.notification_button),
                contentPendingIntent
            ).setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)


        notify(NOTIFICATION_ID, builder.build())


}






