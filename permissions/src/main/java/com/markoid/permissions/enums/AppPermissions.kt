package com.markoid.permissions.enums

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

sealed class AppPermissions(val manifestPermissions: Array<String>) {

    object Camera : AppPermissions(arrayOf(Manifest.permission.CAMERA))

    object Contact : AppPermissions(arrayOf(Manifest.permission.GET_ACCOUNTS))

    object Microphone : AppPermissions(arrayOf(Manifest.permission.RECORD_AUDIO))

    object Phone : AppPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE))

    object Storage : AppPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))

    object RegularLocation : AppPermissions(
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    object BackgroundLocation :
        AppPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
}
