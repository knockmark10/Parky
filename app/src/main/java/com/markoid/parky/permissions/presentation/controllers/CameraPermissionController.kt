package com.markoid.parky.permissions.presentation.controllers

import android.content.pm.PackageManager
import com.markoid.parky.R
import com.markoid.parky.core.presentation.dispatchers.CoroutineDispatcherProvider
import com.markoid.parky.core.presentation.providers.ActivityProvider
import com.markoid.parky.permissions.presentation.extensions.hasCameraPermission
import com.markoid.permissions.enums.AppPermissions
import com.markoid.permissions.managers.abstractions.PermissionManager
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CameraPermissionController @Inject constructor(
    activityProvider: ActivityProvider,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val permissionManager: PermissionManager
) : AbstractPermissionController<Unit>(activityProvider) {

    override val permissionDeniedMessage: Int
        get() = R.string.camera_permission_denied

    override val rationaleTitle: Int
        get() = R.string.camera_permission_title

    override val rationaleMessage: Int
        get() = R.string.camera_permission_rationale

    override suspend fun onRequestPermission(params: Unit): Boolean {
        // Make sure that camera is available
        if (isCameraHardwareAvailable().not()) return false
        // Check camera permission
        if (permissionManager.hasCameraPermission) return true
        // Request camera permission
        val result = withContext(dispatcherProvider.main) {
            permissionManager.requestPermission(AppPermissions.Camera)
        }
        return handlePermissionResult(result, Unit)
    }

    private fun isCameraHardwareAvailable(): Boolean =
        activityProvider.currentActivity
            ?.packageManager
            ?.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
            ?: false
}
