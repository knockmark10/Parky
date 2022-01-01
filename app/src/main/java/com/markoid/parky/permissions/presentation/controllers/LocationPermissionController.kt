package com.markoid.parky.permissions.presentation.controllers

import android.annotation.TargetApi
import android.os.Build
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markoid.parky.R
import com.markoid.parky.core.presentation.dispatchers.CoroutineDispatcherProvider
import com.markoid.parky.core.presentation.providers.ActivityProvider
import com.markoid.parky.permissions.presentation.enums.LocationPermissions
import com.markoid.parky.permissions.presentation.extensions.hasBackgroundPermission
import com.markoid.parky.permissions.presentation.extensions.locationPermissionsFor29
import com.markoid.permissions.enums.AppPermissions
import com.markoid.permissions.extensions.isAtLeastApi29
import com.markoid.permissions.extensions.isAtLeastApi30
import com.markoid.permissions.managers.abstractions.PermissionManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * This will handle all the logic involved in request location permissions, while handling the
 * different approaches for the different API versions.
 */
class LocationPermissionController @Inject constructor(
    activityProvider: ActivityProvider,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val permissionManager: PermissionManager
) : AbstractPermissionController<LocationPermissions>(activityProvider) {

    override val permissionDeniedMessage: Int
        get() = R.string.location_permission_denied

    override val rationaleTitle: Int
        get() = R.string.location_permission_title

    override val rationaleMessage: Int
        get() = R.string.location_permission_rationale

    override val rationalePositiveButton: Int
        get() = R.string.grant

    override suspend fun onRequestPermission(params: LocationPermissions): Boolean =
        when (params) {
            LocationPermissions.RegularLocation -> requestRegularLocation(params)
            LocationPermissions.BackgroundLocation -> when {
                isAtLeastApi30 -> requestBackgroundLocationForApi30(params)
                isAtLeastApi29 -> requestBackgroundLocationForApi29(params)
                else -> requestRegularLocation(params)
            }
        }

    /**
     * Requests [ACCESS_COARSE_LOCATION] and [ACCESS_FINE_LOCATION] permissions. They don't require
     * special considerations.
     */
    @TargetApi(Build.VERSION_CODES.P)
    private suspend fun requestRegularLocation(params: LocationPermissions): Boolean {
        val locationPermissions = AppPermissions.RegularLocation
        // Check if permission is granted before hand
        if (permissionManager.hasPermission(locationPermissions)) return true
        val result = withContext(dispatcherProvider.main) {
            permissionManager.requestPermission(locationPermissions)
        }
        return handlePermissionResult(result, params)
    }

    /**
     * Requests [ACCESS_COARSE_LOCATION], [_ACCESS_FINE_LOCATION] and [ACCESS_BACKGROUND_LOCATION].
     * The three of them are required for the background location.
     */
    @TargetApi(Build.VERSION_CODES.Q)
    private suspend fun requestBackgroundLocationForApi29(params: LocationPermissions): Boolean {
        if (permissionManager.hasBackgroundPermission) return true
        val result = withContext(dispatcherProvider.main) {
            permissionManager.requestPermissions(*locationPermissionsFor29)
        }
        return handlePermissionResult(result, params)
    }

    /**
     * Requests [ACCESS_BACKGROUND_LOCATION]. They way to do it is by showing a rationale dialog
     * and taking the user to settings.
     */
    @TargetApi(Build.VERSION_CODES.R)
    private suspend fun requestBackgroundLocationForApi30(params: LocationPermissions): Boolean {
        // Check if background permission is granted
        if (permissionManager.hasPermission(AppPermissions.BackgroundLocation)) return true

        return when {
            // Check if regular location has been granted. If it is, request the background one
            permissionManager.hasPermission(AppPermissions.RegularLocation) ->
                showBackgroundLocationRationale(params)

            // Request regular location permission. If it gets granted, request the background one
            requestRegularLocation(params) -> showBackgroundLocationRationale(params)

            // Permissions were not granted
            else -> showDenialSnackBar()
        }
    }

    private suspend fun showBackgroundLocationRationale(params: LocationPermissions): Boolean {
        val activity = activityProvider.currentActivity ?: return false
        return try {
            withContext(activity.lifecycleScope.coroutineContext) {
                suspendCancellableCoroutine { continuation ->
                    val dialog = MaterialAlertDialogBuilder(activity)
                        .setTitle(R.string.location_background_permission_title)
                        .setMessage(R.string.location_background_permission_rationale)
                        .setPositiveButton(R.string.accept) { _, _ ->
                            activity.lifecycleScope.launch {
                                val result =
                                    permissionManager.requestPermission(AppPermissions.BackgroundLocation)
                                val wasGranted = handlePermissionResult(result, params)
                                if (continuation.isActive) {
                                    continuation.resume(wasGranted)
                                }
                            }
                        }
                        .setNegativeButton(R.string.cancel, null)
                        .setOnDismissListener {
                            if (continuation.isActive) {
                                continuation.resume(false)
                            }
                        }
                        .setCancelable(false)
                        .show()

                    continuation.invokeOnCancellation { dialog.dismiss() }
                }
            }
        } catch (e: Throwable) {
            // The activity was destroyed
            return false
        }
    }
}
