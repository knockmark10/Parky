package com.markoid.parky.core.presentation.dialogs

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import java.util.* // ktlint-disable no-wildcard-imports

abstract class AbstractDialog<T : ViewBinding> : DialogFragment() {

    private var _binding: T? = null

    val binding: T
        get() = _binding!!

    open fun getStyle(): Int? = null

    abstract fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): T

    private val stateStack: Stack<DialogState> = Stack()

    abstract fun initView(view: View, savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getStyle()?.let { setStyle(STYLE_NORMAL, it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (stateStack.empty() || stateStack.peek() == DialogState.Dismiss) {
            stateStack.push(DialogState.Show)
            super.show(manager, tag)
        } else {
            stateStack.clear()
            stateStack.push(DialogState.Show)
        }
    }

    override fun dismiss() {
        if (stateStack.empty().not() && stateStack.peek() == DialogState.Show) {
            stateStack.push(DialogState.Dismiss)
            super.dismiss()
        } else {
            stateStack.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stateStack.clear()
    }

    open fun getResolvedColor(@ColorRes color: Int): Int =
        ContextCompat.getColor(requireContext(), color)

    open fun getResolvedDrawable(@DrawableRes resId: Int): Drawable? =
        ContextCompat.getDrawable(requireContext(), resId)

    enum class DialogState {
        Show,
        Dismiss
    }
}
