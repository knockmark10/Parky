package com.markoid.parky.core.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class AbstractFragment<T : ViewBinding> : Fragment() {

    private var _binding: T? = null

    /**
     * This property is only valid between onCreateView and onDestroyView
     */
    val binding: T
        get() = _binding!!

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): T

    abstract fun onInitView(view: View, savedInstanceState: Bundle?)

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

        onInitView(view, savedInstanceState)
    }

    open fun getResolvedColor(@ColorRes color: Int): Int =
        ContextCompat.getColor(requireContext(), color)

    open fun getResolvedDrawable(@DrawableRes resId: Int): Drawable? =
        ContextCompat.getDrawable(requireContext(), resId)
}
