package com.markoid.parky.home.presentation.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import com.markoid.parky.core.presentation.extensions.alarmManager
import com.markoid.parky.home.presentation.receivers.NotificationReceiver
import org.joda.time.DateTime

object AlarmUtils {

    fun setAlarm(context: Context, alarmTime: DateTime) {
        // Intent to start Broadcast Receiver
        val broadcastIntent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1001,
            broadcastIntent,
            0
        )

        // Setting up AlarmManager
        val alarmManager: AlarmManager = context.alarmManager

        if (DateTime.now().millis < alarmTime.millis) {
            Log.d("ALARMDEBUG", "Setting alarm")
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                alarmTime.millis,
                pendingIntent
            )
        } else {
            Log.d("ALARMDEBUG", "No alarm has been set")
        }
    }
}
