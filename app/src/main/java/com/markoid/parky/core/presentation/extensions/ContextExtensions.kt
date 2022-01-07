package com.markoid.parky.core.presentation.extensions

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager

val Context.alarmManager: AlarmManager
    get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager

val Context.notificationManager: NotificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

val Context.activityManager: ActivityManager
    get() = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

@Suppress("DEPRECATION") // Deprecated for third party Services.
fun <T> Context.isServiceRunning(service: Class<T>): Boolean = activityManager
    .getRunningServices(Integer.MAX_VALUE)
    ?.find { it.service.className == service.name }
    ?.foreground == true

fun Context.resolveColor(@ColorRes colorId: Int): Int =
    ContextCompat.getColor(this, colorId)

fun Context.resolveDrawable(@DrawableRes drawableId: Int): Drawable? =
    ContextCompat.getDrawable(this, drawableId)

val Context.verticalLayoutManager
    get() = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

fun Context.getAttrColor(resId: Int): Int {
    val typedValue = TypedValue()
    val theme = theme
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}
