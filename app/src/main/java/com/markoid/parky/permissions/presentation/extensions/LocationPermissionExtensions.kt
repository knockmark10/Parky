package com.markoid.parky.permissions.presentation.extensions

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import com.markoid.permissions.enums.AppPermissions.BackgroundLocation
import com.markoid.permissions.enums.AppPermissions.RegularLocation
import com.markoid.permissions.managers.abstractions.PermissionManager

val locationPermissionsFor29: Array<String>
    @RequiresApi(Build.VERSION_CODES.Q)
    get() = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

val PermissionManager.hasBackgroundPermission: Boolean
    @RequiresApi(Build.VERSION_CODES.Q)
    get() = hasPermission(RegularLocation) && hasPermission(BackgroundLocation)
