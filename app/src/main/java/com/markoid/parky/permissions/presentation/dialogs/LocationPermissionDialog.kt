package com.markoid.parky.permissions.presentation.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.markoid.parky.R
import com.markoid.parky.core.presentation.dialogs.AbstractDialog
import com.markoid.parky.databinding.DialogLocationPermissionBinding
import com.markoid.parky.permissions.presentation.dialogs.abstraction.IPermissionDialog

class LocationPermissionDialog :
    AbstractDialog<DialogLocationPermissionBinding>(),
    IPermissionDialog {

    private var onDismissListener: IPermissionDialog.OnDismissListener? = null

    private var onGrantAccessListener: IPermissionDialog.OnGrantAccessListener? = null

    override fun getStyle(): Int = R.style.FullWidthDialog

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogLocationPermissionBinding =
        DialogLocationPermissionBinding.inflate(layoutInflater, container, false)

    override fun initView(view: View, savedInstanceState: Bundle?) {
    }

    override fun setOnDismissListener(listener: IPermissionDialog.OnDismissListener): LocationPermissionDialog {
        onDismissListener = listener
        return this
    }

    override fun setOnGrantAccessListener(listener: IPermissionDialog.OnGrantAccessListener): LocationPermissionDialog {
        onGrantAccessListener = listener
        return this
    }

    override fun display(): IPermissionDialog {
        super.show(childFragmentManager, this::class.java.name)
        return this
    }

    override fun hide() {
        super.dismiss()
    }

    override fun setTitle(title: String): IPermissionDialog {

        return this
    }

    override fun setMessage(message: String): IPermissionDialog {

        return this
    }

    override fun onDismiss(dialog: DialogInterface) {
        this.onDismissListener?.onDismiss(this)
        super.onDismiss(dialog)
    }
}
