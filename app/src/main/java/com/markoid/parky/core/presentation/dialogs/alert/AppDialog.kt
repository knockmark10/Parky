package com.markoid.parky.core.presentation.dialogs.alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.markoid.parky.core.presentation.dialogs.AbstractBottomSheetDialog
import com.markoid.parky.core.presentation.enums.AlertType
import com.markoid.parky.databinding.DialogAppBinding

class AppDialog : AbstractBottomSheetDialog<DialogAppBinding>(), AppDialogInterface {

    private var isShowing: Boolean = false

    override var isDialogCancelable: Boolean = false

    override val isDialogShowing: Boolean
        get() = isShowing

    override var message: String = ""

    override var type: AlertType = AlertType.Error

    override var positiveListener: OnPositiveListener? = null

    override var negativeListener: OnNegativeListener? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogAppBinding = DialogAppBinding.inflate(inflater, container, false)

    override fun initView(view: View, savedInstanceState: Bundle?) {
        isCancelable = isDialogCancelable
        binding.alertIcon.setImageDrawable(getIcon(type.iconId))
        binding.alertTitle.text = getString(type.titleId)
        binding.alertPositiveButton.setBackgroundColor(getColor(type.colorId))
        binding.alertPositiveButton.setRippleColorResource(type.darkColorId)
        binding.alertMessage.text = message
        binding.alertNegativeButton.isVisible = negativeListener != null
        binding.alertPositiveButton
            .setOnClickListener { positiveListener?.invoke(this) ?: dismiss() }
        binding.alertNegativeButton
            .setOnClickListener { negativeListener?.invoke(this) ?: dismiss() }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        isShowing = true
        super.show(manager, tag)
    }

    override fun close() {
        super.dismiss()
        isShowing = false
    }
}
