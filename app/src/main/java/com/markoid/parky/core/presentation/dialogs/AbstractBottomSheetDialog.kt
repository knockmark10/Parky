package com.markoid.parky.core.presentation.dialogs

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.markoid.parky.R

abstract class AbstractBottomSheetDialog<T : ViewBinding> : BottomSheetDialogFragment() {

    private var _binding: T? = null

    val binding: T
        get() = _binding!!

    abstract fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): T

    abstract fun initView(view: View, savedInstanceState: Bundle?)

    override fun getTheme(): Int = R.style.Theme_Design_BottomSheetDialog

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

    fun getIcon(@DrawableRes drawableId: Int): Drawable? =
        ContextCompat.getDrawable(requireContext(), drawableId)

    fun getColor(@ColorRes colorId: Int): Int =
        ContextCompat.getColor(requireContext(), colorId)
}
