package com.markoid.parky.home.presentation.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import com.markoid.parky.core.presentation.extensions.alarmManager
import com.markoid.parky.home.presentation.receivers.AlarmNotificationReceiver
import org.joda.time.DateTime

object AlarmUtils {

    fun setAlarm(context: Context, alarmTime: DateTime) {
        // Intent to start Broadcast Receiver
        val broadcastIntent = Intent(context, AlarmNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1001,
            broadcastIntent,
            0
        )

        // Setting up AlarmManager
        val alarmManager: AlarmManager = context.alarmManager

        if (DateTime.now().millis < alarmTime.millis) {
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                alarmTime.millis,
                pendingIntent
            )
        }
    }
}
