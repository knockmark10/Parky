package com.markoid.parky.core.presentation.extensions

import com.markoid.parky.home.presentation.viewmodels.entities.RemainingTime
import org.joda.time.Duration

fun Duration.toTimer(): RemainingTime {
    var timer = Duration(millis)
    val remainingTime = RemainingTime()
    if (timer.standardDays >= 1) {
        remainingTime.days = timer.standardDays
        timer = timer.minus(Duration.standardDays(timer.standardDays))
    }
    if (timer.standardHours >= 1) {
        remainingTime.hours = timer.standardHours
        timer = timer.minus(Duration.standardHours(timer.standardHours))
    }
    if (timer.standardMinutes >= 1) {
        remainingTime.minutes = timer.standardMinutes
        timer = timer.minus(Duration.standardMinutes(timer.standardMinutes))
    }
    remainingTime.seconds = timer.standardSeconds
    return remainingTime
}
