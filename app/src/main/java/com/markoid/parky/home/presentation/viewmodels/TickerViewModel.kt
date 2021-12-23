package com.markoid.parky.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markoid.parky.core.presentation.extensions.toTimer
import com.markoid.parky.home.presentation.viewmodels.entities.RemainingTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.joda.time.DateTime
import org.joda.time.Duration
import javax.inject.Inject

@HiltViewModel
class TickerViewModel @Inject constructor() : ViewModel() {

    fun getTimer(alarmTime: DateTime): SharedFlow<RemainingTime> =
        tickerFlow(Duration(DateTime.now(), alarmTime))
            .distinctUntilChanged { old, new -> old == new }
            .map { Duration(DateTime.now(), alarmTime) }
            .map { it.toTimer() }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily
            )

    private fun tickerFlow(
        timerDuration: Duration,
        initialDelay: Duration = Duration.standardSeconds(1),
        period: Duration = Duration.standardSeconds(1)
    ) = flow {
        var initialValue = 1
        delay(initialDelay.millis)
        while (initialValue <= timerDuration.standardSeconds) {
            emit(initialValue++)
            delay(period.millis)
        }
    }
}
