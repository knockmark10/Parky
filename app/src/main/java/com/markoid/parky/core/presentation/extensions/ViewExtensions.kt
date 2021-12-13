package com.markoid.parky.core.presentation.extensions

import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.markoid.parky.core.presentation.views.InstantView

val AppCompatEditText.value: String
    get() = text?.toString()?.trim() ?: ""

val InstantView.value: String
    get() = text?.toString()?.trim() ?: ""

fun DialogFragment.show(fragmentManager: FragmentManager) {
    show(fragmentManager, this::class.java.name)
}
