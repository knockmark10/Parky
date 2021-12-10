package com.markoid.permissions.managers.abstractions

import com.markoid.permissions.enums.AppPermissions
import com.markoid.permissions.enums.PermissionStatus
import com.markoid.permissions.managers.implementations.PermissionManagerImpl

interface PermissionManager {
    companion object {
        fun getInstance(): PermissionManager = PermissionManagerImpl
    }

    /**
     * Determine whether <em>you</em> have been granted a particular permission.
     *
     * @param permission The name of the permission being checked.
     * @return a [Boolean] to indicate the current status.
     */
    fun hasPermission(permission: AppPermissions): Boolean

    /**
     * Determine whether <em>you</em> have been granted a particular permission.
     *
     * @param permission The name of the permission being checked.
     * @return a [Boolean] to indicate the current status.
     */
    fun hasPermission(permission: String): Boolean

    /**
     * Request a single permission.
     *
     * @param permissions The [AppPermissions] type of the permission to be requested.
     * @return a [PermissionStatus] to indicate the current status.
     *
     * @see [com.markoid.permissions.helpers.requestPermission]
     */
    suspend fun requestPermission(permissions: AppPermissions): Map<String, PermissionStatus>

    /**
     * Request multiple permission.
     *
     * @param permissions the names of permissions to be requested.
     * @return a [Map] containing the [PermissionStatus] of the request indexed by the
     * permission name
     *
     * @see [com.markoid.permissions.helpers.requestPermissions]
     */
    suspend fun requestPermissions(vararg permissions: String): Map<String, PermissionStatus>
}
