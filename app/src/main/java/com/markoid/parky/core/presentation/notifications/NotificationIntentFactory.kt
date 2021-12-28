package com.markoid.parky.core.presentation.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.markoid.parky.core.presentation.notifications.NotificationConstants.PARKING_SPOT_REQUEST_ARG
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import com.markoid.parky.home.presentation.activities.HomeActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val AUTO_PARKING_REQUEST_CODE = 1002
private const val BLUETOOTH_REQUEST_CODE = 1003
private const val REMINDER_NOTIFICATION_REQUEST_CODE = 1001

class NotificationIntentFactory
@Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val homeActivityIntent = Intent(context, HomeActivity::class.java)

    fun getPendingIntentByNotificationType(
        type: AppNotificationType,
        request: ParkingSpotRequest? = null
    ): PendingIntent = when (type) {
        AppNotificationType.AutoParkingSpotMissingData -> getAutoParkingMissingDataIntent(request!!)
        AppNotificationType.AutoParkingSpotSuccessful -> getAutoParkingSavedIntent()
        AppNotificationType.Bluetooth -> getBluetoothIntent()
        AppNotificationType.ReminderAlarm -> getReminderAlarmIntent()
    }

    private fun getAutoParkingMissingDataIntent(
        incompleteRequest: ParkingSpotRequest
    ): PendingIntent {
        val redirectIntent = homeActivityIntent
            .apply {
                action = NotificationIntentActions.ACTION_ADD_PARKING.action
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(PARKING_SPOT_REQUEST_ARG, incompleteRequest)
            }
        return PendingIntent.getActivity(
            context,
            AUTO_PARKING_REQUEST_CODE,
            redirectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getAutoParkingSavedIntent(): PendingIntent {
        val redirectIntent = homeActivityIntent
            .apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP }
        return PendingIntent.getActivity(
            context,
            AUTO_PARKING_REQUEST_CODE,
            redirectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getBluetoothIntent(): PendingIntent {
        val redirectIntent = homeActivityIntent
            .apply {
                action = NotificationIntentActions.ACTION_SETTINGS.action
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        return PendingIntent.getActivity(
            context,
            BLUETOOTH_REQUEST_CODE,
            redirectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getReminderAlarmIntent(): PendingIntent {
        val redirectIntent = homeActivityIntent
            .apply {
                action = NotificationIntentActions.ACTION_USER_LOCATION.action
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        return PendingIntent.getActivity(
            context,
            REMINDER_NOTIFICATION_REQUEST_CODE,
            redirectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}
