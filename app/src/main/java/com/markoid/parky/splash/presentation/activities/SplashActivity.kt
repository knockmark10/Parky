package com.markoid.parky.splash.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.markoid.parky.core.presentation.activities.AbstractActivity
import com.markoid.parky.databinding.ActivitySplashBinding
import com.markoid.parky.home.presentation.activities.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AbstractActivity<ActivitySplashBinding>() {

    override fun getViewBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        startActivity(Intent(this, HomeActivity::class.java))
    }
}
