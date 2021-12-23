package com.markoid.parky.home.presentation.viewmodels.entities

data class RemainingTime(
    var days: Long = 0L,
    var hours: Long = 0L,
    var minutes: Long = 0L,
    var seconds: Long = 0L
) {

    fun isTimeUp(): Boolean =
        days == 0L && hours == 0L && minutes == 0L && seconds == 0L
}
