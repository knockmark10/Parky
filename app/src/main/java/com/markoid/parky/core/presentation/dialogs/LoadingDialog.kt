package com.markoid.parky.core.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.markoid.parky.R
import com.markoid.parky.databinding.DialogLoadingBinding

class LoadingDialog : AbstractDialog<DialogLoadingBinding>() {

    override fun getStyle(): Int = R.style.FullScreenDialog

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogLoadingBinding = DialogLoadingBinding.inflate(inflater, container, false)

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.loadingAnim.playAnimation()
    }
}