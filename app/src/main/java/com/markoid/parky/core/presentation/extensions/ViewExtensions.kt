package com.markoid.parky.core.presentation.extensions

import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.PopupMenu
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

fun View.popUpMenuOnClickListener(@MenuRes menuId: Int, block: (MenuItem) -> Unit) {
    val popUpMenu = PopupMenu(context, this).apply { inflate(menuId) }
    setOnClickListener {
        popUpMenu.show()
        popUpMenu.setOnMenuItemClickListener {
            block(it)
            true
        }
    }
}
