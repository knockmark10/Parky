package com.markoid.permissions.enums

sealed class PermissionStatus

/**
 * @see [PERMISSION_GRANTED]
 */
object PermissionGranted : PermissionStatus()

/**
 * @see [PERMISSION_DENIED]
 * @see [Activity.shouldShowRequestPermissionRationale]
 */
data class PermissionDenied(val shouldShowRationale: Boolean) : PermissionStatus()
