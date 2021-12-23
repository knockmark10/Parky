package com.markoid.parky.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.iigo.library.ClockHelper
import com.markoid.parky.R
import com.markoid.parky.core.presentation.dialogs.AbstractDialog
import com.markoid.parky.core.presentation.extensions.launchWhenStartedCatching
import com.markoid.parky.core.presentation.extensions.toDigits
import com.markoid.parky.core.presentation.extensions.toTimer
import com.markoid.parky.databinding.DialogAlarmBinding
import com.markoid.parky.home.presentation.viewmodels.TickerViewModel
import com.markoid.parky.home.presentation.viewmodels.entities.RemainingTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.joda.time.DateTime
import org.joda.time.Duration

class AlarmDialog : AbstractDialog<DialogAlarmBinding>() {

    private val tickerViewModel by viewModels<TickerViewModel>()

    private val alarmTime: DateTime
        get() = DateTime(arguments?.getLong(ALARM_ARG, 0L) ?: 0L)

    override fun getStyle(): Int = R.style.FullWidthDialog

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogAlarmBinding = DialogAlarmBinding.inflate(inflater, container, false)

    override fun initView(view: View, savedInstanceState: Bundle?) {

        setInitialData()

        setClickListener()

        startTicker()
    }

    private fun setInitialData() {
        if (alarmTime.isAfterNow) {
            val timer = Duration(DateTime.now(), alarmTime).toTimer()
            binding.clock.setTime(timer.hours.toInt(), timer.minutes.toInt(), timer.seconds.toInt())
            binding.daysValue.text = timer.days.toDigits()
            binding.hoursValue.text = timer.hours.toDigits()
            binding.minutesValue.text = timer.minutes.toDigits()
            binding.secondsValue.text = timer.seconds.toDigits()
        } else {
            val helper = ClockHelper(binding.clock)
            helper.goOff()
        }
    }

    private fun startTicker() {
        viewLifecycleOwner.lifecycleScope.launchWhenStartedCatching(
            lifecycle,
            { observeTicker() },
            { it.printStackTrace() }
        )
    }

    private fun setClickListener() {
        binding.actionAccept.setOnClickListener { dismiss() }
    }

    private fun CoroutineScope.observeTicker() {
        tickerViewModel.getTimer(alarmTime)
            .onEach { setAlarmData(it) }
            .launchIn(this)
    }

    private fun setAlarmData(data: RemainingTime) = with(binding) {
        val helper = ClockHelper(binding.clock)
        binding.clock.setTime(data.hours.toInt(), data.minutes.toInt(), data.seconds.toInt())
        binding.daysValue.text = data.days.toDigits()
        binding.hoursValue.text = data.hours.toDigits()
        binding.minutesValue.text = data.minutes.toDigits()
        binding.secondsValue.text = data.seconds.toDigits()
        if (data.isTimeUp()) helper.goOff()
    }

    companion object {
        private const val ALARM_ARG = "alarm.arg"
        fun newInstance(alarmTime: DateTime): AlarmDialog = AlarmDialog().apply {
            arguments = Bundle().apply { putLong(ALARM_ARG, alarmTime.millis) }
        }
    }
}
