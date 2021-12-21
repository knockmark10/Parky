package com.markoid.parky.home.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_TIME_CHANGED
import com.markoid.parky.home.presentation.controllers.AlarmController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TimeChangedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmController: AlarmController

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            if (intent?.action == ACTION_TIME_CHANGED) {
                alarmController.setAlarm(it)
            }
        }
    }
}
