package com.markoid.parky.permissions.domain.exceptions

import com.markoid.permissions.enums.AppPermissions
import java.io.IOException

class PermissionNotGrantedException(
    permission: AppPermissions
) : IOException("${permission.manifestPermissions[0]} permission not granted.")
