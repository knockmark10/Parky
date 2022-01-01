package com.markoid.parky.core.presentation.extensions

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.markoid.parky.core.presentation.dialogs.alert.AppDialog
import com.markoid.parky.core.presentation.dialogs.alert.AppDialogInterface
import com.markoid.parky.core.presentation.entities.XDate
import com.markoid.parky.core.presentation.entities.XTime
import org.joda.time.DateTime

@Suppress("UNCHECKED_CAST")
fun <F : Fragment> AppCompatActivity.findFragmentByClassName(fragmentClass: Class<F>): F? {
    val navHostFragment = supportFragmentManager.primaryNavigationFragment as? NavHostFragment
    val desiredFragment = navHostFragment?.childFragmentManager?.primaryNavigationFragment as? F
    if (desiredFragment != null) {
        return desiredFragment
    } else {
        navHostFragment?.childFragmentManager?.fragments?.forEach {
            if (fragmentClass.isAssignableFrom(it.javaClass)) {
                return it as? F
            }
        }
    }
    return null
}

fun Fragment.timePicker(
    is24HourView: Boolean,
    block: TimePicker.(XTime) -> Unit,
    cancelBlock: DialogInterface.() -> Unit = {}
): TimePickerDialog {
    val now = DateTime.now()
    return TimePickerDialog(
        requireContext(),
        { timePicker, hourOfDay, minute ->
            block(timePicker, XTime(hourOfDay, minute))
        },
        now.hourOfDay,
        now.minuteOfHour,
        is24HourView
    ).apply { setOnCancelListener { cancelBlock(it) } }
}

fun Fragment.datePicker(
    block: DatePicker.(XDate) -> Unit,
    cancelBlock: DialogInterface.() -> Unit = {}
): DatePickerDialog {
    val now = DateTime.now()
    return DatePickerDialog(
        requireContext(),
        { datePicker, year, month, dayOfMonth ->
            block(datePicker, XDate(dayOfMonth, month, year))
        },
        now.year,
        now.monthOfYear - 1,
        now.dayOfMonth
    ).apply { setOnCancelListener { cancelBlock(it) } }
}

fun Fragment.dateAndTimePickers(
    is24HourView: Boolean,
    onSelectedBlock: DateTime.() -> Unit,
    cancelBlock: () -> Unit = {}
) {
    datePicker({ date: XDate ->
        timePicker(is24HourView, { time: XTime ->
            onSelectedBlock(
                DateTime(
                    date.year,
                    date.month + 1,
                    date.dayOfMonth,
                    time.hourOfDay,
                    time.minute
                )
            )
        }, {
            cancelBlock()
        }).show()
    }, {
        cancelBlock()
    }).show()
}

fun Fragment.shortToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Fragment.appAlert(block: AppDialogInterface.() -> Unit) {
    val appDialog = AppDialog()
    appDialog.show(childFragmentManager, this::class.java.name)
    block(appDialog)
}

fun Fragment.ensureAdded(contract: Fragment.() -> Unit) {
    if (isAdded) contract(this)
}
