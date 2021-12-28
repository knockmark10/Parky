package com.markoid.parky.home.presentation.services

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.markoid.parky.core.presentation.notifications.AppNotificationManager
import com.markoid.parky.home.presentation.receivers.BluetoothReceiver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val BLUETOOTH_SERVICE_ID = 92837

/**
 * This service will be responsible for registering a new parking spot when the bluetooth
 * gets disconnected from user's radio.
 */
@AndroidEntryPoint
class BluetoothService : Service() {

    @Inject
    lateinit var notificationManager: AppNotificationManager

    private val bluetoothReceiver by lazy { BluetoothReceiver() }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action?.equals(ACTION_START_SERVICE) == true) start()
        else stop()
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(bluetoothReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bluetoothReceiver)
    }

    private fun start() {
        startForeground(
            BLUETOOTH_SERVICE_ID,
            notificationManager.getBluetoothServiceNotification()
        )
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    companion object {
        private const val ACTION_START_SERVICE = "action.start.foreground"
        private const val ACTION_STOP_SERVICE = "action.stop.foreground"

        fun startBluetoothService(context: Context) {
            val startIntent = Intent(context, BluetoothService::class.java)
            startIntent.action = ACTION_START_SERVICE
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopBluetoothService(context: Context) {
            val stopIntent = Intent(context, BluetoothService::class.java)
            stopIntent.action = ACTION_STOP_SERVICE
            ContextCompat.startForegroundService(context, stopIntent)
        }
    }
}
