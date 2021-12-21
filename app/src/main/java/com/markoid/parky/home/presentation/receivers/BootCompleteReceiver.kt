package com.markoid.parky.home.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED
import com.markoid.parky.home.presentation.controllers.AlarmController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * This receiver will get triggered when the phone completes its reboot cycle.
 */
@AndroidEntryPoint
class BootCompleteReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmController: AlarmController

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            if (intent?.action == ACTION_BOOT_COMPLETED) {
                alarmController.setAlarm(it)
            }
        }
    }
}
