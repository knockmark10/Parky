package com.markoid.parky.onboarding.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.github.appintro.SlideBackgroundColorHolder
import com.markoid.parky.core.presentation.AbstractFragment
import com.markoid.parky.databinding.FragmentOnboardingBinding

class OnBoardingFragment :
    AbstractFragment<FragmentOnboardingBinding>(),
    SlideBackgroundColorHolder {

    private val titleId: Int
        get() = arguments?.getInt(TITLE_ID_ARG, 0) ?: 0

    private val descriptionId: Int
        get() = arguments?.getInt(DESCRIPTION_ID_ARG, 0) ?: 0

    private val animationFile: String
        get() = arguments?.getString(ANIMATION_FILE_ARG, "") ?: ""

    private val backgroundColor: Int
        get() = arguments?.getInt(BACKGROUND_COLOR, 0) ?: 0

    override val defaultBackgroundColor: Int
        get() = ContextCompat.getColor(requireContext(), backgroundColor)

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOnboardingBinding = FragmentOnboardingBinding
        .inflate(layoutInflater, container, false)

    override fun onInitView(view: View, savedInstanceState: Bundle?) {
        binding.title.text = getString(titleId)
        binding.description.text = getString(descriptionId)
        binding.image.setAnimation(animationFile)
        binding.image.playAnimation()
    }

    companion object {
        private const val TITLE_ID_ARG = "title.id.arg"
        private const val DESCRIPTION_ID_ARG = "message.id.arg"
        private const val ANIMATION_FILE_ARG = "animation.file.arg"
        private const val BACKGROUND_COLOR = "background.color.arg"
        fun newInstance(
            @StringRes titleResId: Int,
            @StringRes descriptionId: Int,
            animationFile: String,
            @ColorRes backgroundColorId: Int
        ): OnBoardingFragment = OnBoardingFragment().apply {
            arguments = Bundle().apply {
                putInt(TITLE_ID_ARG, titleResId)
                putInt(DESCRIPTION_ID_ARG, descriptionId)
                putString(ANIMATION_FILE_ARG, animationFile)
                putInt(BACKGROUND_COLOR, backgroundColorId)
            }
        }
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        binding.root.setBackgroundColor(backgroundColor)
    }
}
