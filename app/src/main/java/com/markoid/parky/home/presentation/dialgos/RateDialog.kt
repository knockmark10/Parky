package com.markoid.parky.home.presentation.dialgos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.markoid.parky.R
import com.markoid.parky.core.presentation.dialogs.AbstractDialog
import com.markoid.parky.databinding.DialogRateBinding

class RateDialog : AbstractDialog<DialogRateBinding>() {

    override fun getStyle(): Int = R.style.FullScreenDialog

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogRateBinding = DialogRateBinding.inflate(inflater, container, false)

    override fun initView(view: View, savedInstanceState: Bundle?) {
    }
}
