package com.markoid.parky.onboarding.activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroPageTransformerType
import com.markoid.parky.R
import com.markoid.parky.home.presentation.activities.HomeActivity
import com.markoid.parky.onboarding.fragments.OnBoardingFragment
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Reference: https://github.com/AppIntro/AppIntro
 */
@AndroidEntryPoint
class OnBoardingActivity : AppIntro() {

    @Inject
    lateinit var devicePreferences: DevicePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransformer(AppIntroPageTransformerType.Parallax())
        isColorTransitionsEnabled = true
        addSlide(
            OnBoardingFragment.newInstance(
                titleResId = R.string.onboarding_welcome_title,
                descriptionId = R.string.onboarding_welcome_description,
                animationFile = "parking_onboarding.json",
                backgroundColorId = R.color.md_indigo_400
            )
        )
        addSlide(
            OnBoardingFragment.newInstance(
                titleResId = R.string.onboarding_auto_detection_title,
                descriptionId = R.string.onboarding_auto_detection_description,
                animationFile = "bluetooth_onboarding.json",
                backgroundColorId = R.color.md_indigo_400
            )
        )
        addSlide(
            OnBoardingFragment.newInstance(
                titleResId = R.string.onboarding_reminder_title,
                descriptionId = R.string.onboarding_reminder_description,
                animationFile = "reminder_onboarding.json",
                backgroundColorId = R.color.md_indigo_400
            )
        )
        addSlide(
            OnBoardingFragment.newInstance(
                titleResId = R.string.onboarding_customization_title,
                descriptionId = R.string.onboarding_customization_description,
                animationFile = "customization_onboarding.json",
                backgroundColorId = R.color.md_indigo_400
            )
        )
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        goToHome()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        devicePreferences.onBoardingCompleted = true
        goToHome()
    }

    private fun goToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
