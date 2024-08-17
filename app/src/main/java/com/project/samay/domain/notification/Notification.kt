package com.project.samay.domain.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.project.samay.R
import com.project.samay.domain.service.ServiceHelper
import com.project.samay.util.Constants

object NotificationModule {
    fun provideNotificationBuilder(context: Context): NotificationCompat.Builder{
        return NotificationCompat.Builder(
            context,
            Constants.NOTIFICATION_CHANNEL_ID,
        ).setContentText("StopWatch")
            .setContentText("00:00:00")
            .setSmallIcon(R.drawable.baseline_timer_24)
            .setOngoing(true)
            .addAction(0, "Stop", ServiceHelper.stopPendingIntent(context))
            .addAction(0, "Cancel", ServiceHelper.cancelPendingIntent(context))
            .setContentIntent(ServiceHelper.clickPendingIntent(context))
    }

    fun provideNotificationManager(context: Context): NotificationManager{
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}