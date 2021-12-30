package com.markoid.parky.core.presentation.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

abstract class AbstractActivity<T : ViewBinding> : AppCompatActivity() {

    lateinit var binding: T

    abstract fun getViewBinding(): T

    abstract fun initView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {

        applyTheme()

        super.onCreate(savedInstanceState)

        binding = getViewBinding()

        setContentView(binding.root)

        initView(savedInstanceState)
    }

    open fun getResolvedColor(@ColorRes color: Int): Int =
        ContextCompat.getColor(this, color)

    open fun getResolvedDrawable(@DrawableRes resId: Int): Drawable? =
        ContextCompat.getDrawable(this, resId)

    private fun applyTheme() {
        val activityEntryPoint: BaseActivityEntryPoint =
            EntryPoints.get(applicationContext, BaseActivityEntryPoint::class.java)
        val devicePreferences = activityEntryPoint.getDevicePreferences()
        val themeResId = devicePreferences.themeResId
        setTheme(themeResId)
    }

    /**
     * This would be the application component from Dagger2. Since there is no application component
     * in hilt, this is how you can get those instances that cannot be injected before onCreate.
     */
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface BaseActivityEntryPoint {
        fun getDevicePreferences(): DevicePreferences
    }
}
