package com.markoid.parky.permissions.presentation.dialogs.abstraction

interface IPermissionDialog {

    fun setTitle(title: String): IPermissionDialog

    fun setMessage(message: String): IPermissionDialog

    fun setOnDismissListener(listener: OnDismissListener): IPermissionDialog

    fun setOnGrantAccessListener(listener: OnGrantAccessListener): IPermissionDialog

    /**
     * Interface used to allow the creator of a dialog to run some code when the
     * dialog is dismissed.
     */
    fun interface OnDismissListener {
        /**
         * This method will be invoked when the dialog is dismissed.
         *
         * @param dialog the dialog that was dismissed will be passed into the
         *               method
         */
        fun onDismiss(dialog: IPermissionDialog)
    }

    fun interface OnGrantAccessListener {

        fun onGrantAccess(dialog: IPermissionDialog)
    }

    fun display(): IPermissionDialog

    fun hide()
}
