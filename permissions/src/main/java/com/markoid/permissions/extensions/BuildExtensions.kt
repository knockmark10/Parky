package com.markoid.permissions.extensions

import android.os.Build

val isBelowApi29: Boolean
    get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q

val isAtLeastApi29: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

val isAtLeastApi30: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
