package com.markoid.parky.core.presentation.extensions

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager

val Context.alarmManager: AlarmManager
    get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager

val Context.notificationManager: NotificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

fun Context.resolveColor(@ColorRes colorId: Int): Int =
    ContextCompat.getColor(this, colorId)

fun Context.resolveDrawable(@DrawableRes drawableId: Int): Drawable? =
    ContextCompat.getDrawable(this, drawableId)

val Context.verticalLayoutManager
    get() = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
