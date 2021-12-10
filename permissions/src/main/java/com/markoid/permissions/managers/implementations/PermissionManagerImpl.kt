package com.markoid.permissions.managers.implementations

import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.markoid.permissions.enums.AppPermissions
import com.markoid.permissions.enums.PermissionDenied
import com.markoid.permissions.enums.PermissionGranted
import com.markoid.permissions.enums.PermissionStatus
import com.markoid.permissions.helpers.requestPermission
import com.markoid.permissions.helpers.requestPermissions
import com.markoid.permissions.managers.abstractions.ActivityResultManager
import com.markoid.permissions.managers.abstractions.PermissionManager
import com.markoid.permissions.providers.ActivityProvider

internal object PermissionManagerImpl : PermissionManager {

    override fun hasPermission(permission: AppPermissions): Boolean {
        var granted = true
        permission.manifestPermissions.forEach { granted = granted && hasPermission(it) }
        return granted
    }

    override fun hasPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            ActivityProvider.applicationContext,
            permission
        ) == PERMISSION_GRANTED

    override suspend fun requestPermission(permissions: AppPermissions): Map<String, PermissionStatus> =
        ActivityResultManager.getInstance().requestPermission(permissions)?.let { result ->
            permissions.manifestPermissions.associateWith { permission ->
                if (result[permission] == true) {
                    PermissionGranted
                } else {
                    val shouldShowRationale: Boolean? = ActivityProvider.currentActivity?.let {
                        ActivityCompat
                            .shouldShowRequestPermissionRationale(
                                it,
                                permissions.manifestPermissions.first()
                            )
                    }
                    PermissionDenied(shouldShowRationale ?: false)
                }
            }
        } ?: permissions.manifestPermissions.associateWith { permission ->
            if (hasPermission(permission)) PermissionGranted
            else PermissionDenied(shouldShowRationale = false)
        }

    override suspend fun requestPermissions(vararg permissions: String): Map<String, PermissionStatus> =
        ActivityResultManager.getInstance().requestPermissions(*permissions)?.let { result ->
            permissions.associateWith { permission ->
                if (result[permission] == true) {
                    PermissionGranted
                } else {
                    val shouldShowRationale = ActivityProvider.currentActivity?.let {
                        ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
                    }
                    PermissionDenied(shouldShowRationale ?: false)
                }
            }
        } ?: permissions.associateWith {
            if (hasPermission(it)) PermissionGranted else PermissionDenied(shouldShowRationale = false)
        }
}
