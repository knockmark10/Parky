package com.markoid.parky.home.presentation.receivers

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.presentation.notifications.AppNotificationManager
import com.markoid.parky.home.domain.usecases.response.AutoParkingSpotStatus
import com.markoid.parky.home.presentation.viewmodels.BluetoothViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BluetoothReceiver : BroadcastReceiver() {

    @Inject
    lateinit var coroutineScope: CoroutineScope

    @Inject
    lateinit var notificationManager: AppNotificationManager

    @Inject
    lateinit var viewModel: BluetoothViewModel

    private var deviceName: String = ""

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(BluetoothDevice.ACTION_ACL_CONNECTED, true)) {
            saveBluetoothDeviceName(intent)
        }
        if (intent?.action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED, true)) {
            saveAutoParkingSpot()
        }
    }

    private fun saveAutoParkingSpot() {
        coroutineScope.launch {
            viewModel
                .saveAutoParkingSpot(deviceName)
                .getResult()
                .collect {
                    when (it) {
                        is DataState.Data -> handleSpotSaving(it.data)
                        is DataState.Error -> Log.e("BluetoothReceiver", it.error)
                    }
                }
        }
    }

    private fun handleSpotSaving(result: AutoParkingSpotStatus) {
        when (result) {
            AutoParkingSpotStatus.SkipDisconnectionEvent -> Unit
            is AutoParkingSpotStatus.MissingData ->
                notificationManager.displayAutoParkingMissingDataNotification(result.request)
            is AutoParkingSpotStatus.ParkingSpotSavedAutomatically ->
                notificationManager.displayAutoParkingSavedNotification()
        }
    }

    private fun saveBluetoothDeviceName(intent: Intent?) {
        deviceName = intent
            ?.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            ?.name
            ?: "Unknown device"
    }
}
