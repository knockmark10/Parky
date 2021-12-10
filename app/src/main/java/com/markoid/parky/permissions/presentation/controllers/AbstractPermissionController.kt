package com.markoid.parky.permissions.presentation.controllers

import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.markoid.parky.R
import com.markoid.parky.core.presentation.providers.ActivityProvider
import com.markoid.permissions.enums.PermissionDenied
import com.markoid.permissions.enums.PermissionGranted
import com.markoid.permissions.enums.PermissionStatus
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

abstract class AbstractPermissionController<Params>(val activityProvider: ActivityProvider) {

    /**
     * Message that will be used for letting the user know that the permission was not granted.
     */
    abstract val permissionDeniedMessage: Int

    /**
     * Message that will be used for the rationale dialog title.
     */
    abstract val rationaleTitle: Int

    /**
     * Message that will be used for the rationale dialog message.
     */
    abstract val rationaleMessage: Int

    /**
     * Text that will be displayed for the positive button of the rationale dialog
     */
    open val rationalePositiveButton: Int = R.string.accept

    /**
     * Text that will be displayed for the positive button of the rationale dialog
     */
    open val rationaleNegativeButton: Int = R.string.cancel

    /**
     * This will execute the main task of the controller. It should handle all the edge cases
     * of requesting runtime permissions. This will be executed again if permission is not granted,
     * and user accepts rationale dialog.
     */
    abstract suspend fun onRequestPermission(params: Params): Boolean

    /**
     * It will handle the permission result when requesting the permission.
     */
    suspend fun handlePermissionResult(
        result: Map<String, PermissionStatus>,
        params: Params
    ): Boolean {
        // Check if all permissions were granted
        val isGranted = result.values.all { it == PermissionGranted }
        if (isGranted) return true

        // If we hit this point it means that the permission was not granted.
        return result.values
            // Take the first denied permission available
            .firstOrNull { it !is PermissionGranted }
            // Check for nullability
            ?.let {
                if (it is PermissionDenied && it.shouldShowRationale) {
                    if (showRationale()) onRequestPermission(params) else showDenialSnackBar()
                } else {
                    showDenialSnackBar()
                }
            } ?: showDenialSnackBar()
    }

    /**
     * It will display a rationale dialog to explain the user why this permission is required.
     */
    private suspend fun showRationale(): Boolean {
        val activity = activityProvider.currentActivity ?: return false
        return try {
            withContext(activity.lifecycleScope.coroutineContext) {
                suspendCancellableCoroutine { continuation ->
                    val dialog = MaterialAlertDialogBuilder(activity)
                        .setTitle(rationaleTitle)
                        .setMessage(rationaleMessage)
                        .setPositiveButton(rationalePositiveButton) { _, _ ->
                            continuation.resume(true)
                        }
                        .setNegativeButton(rationaleNegativeButton, null)
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

    /**
     * It will display a snack bar letting the user know that the permission was not granted.
     */
    fun showDenialSnackBar(): Boolean {
        activityProvider.currentActivity?.let {
            Snackbar.make(
                it.findViewById(android.R.id.content),
                permissionDeniedMessage,
                Snackbar.LENGTH_LONG
            ).show()
        }
        return false
    }
}
