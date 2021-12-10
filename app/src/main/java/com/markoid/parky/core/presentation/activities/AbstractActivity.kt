package com.markoid.parky.core.presentation.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding

abstract class AbstractActivity<T : ViewBinding> : AppCompatActivity() {

    lateinit var binding: T

    abstract fun getViewBinding(): T

    abstract fun initView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = getViewBinding()

        setContentView(binding.root)

        initView(savedInstanceState)
    }

    open fun getResolvedColor(@ColorRes color: Int): Int =
        ContextCompat.getColor(this, color)

    open fun getResolvedDrawable(@DrawableRes resId: Int): Drawable? =
        ContextCompat.getDrawable(this, resId)
}
