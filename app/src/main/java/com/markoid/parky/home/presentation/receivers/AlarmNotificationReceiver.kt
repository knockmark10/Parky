package com.markoid.parky.home.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.markoid.parky.core.presentation.notifications.AppNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: AppNotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        displayAlarmNotification()
    }

    private fun displayAlarmNotification() {
        notificationManager.displayReminderAlarmNotification()
    }
}
