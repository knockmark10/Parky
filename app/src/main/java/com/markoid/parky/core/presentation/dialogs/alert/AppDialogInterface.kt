package com.markoid.parky.core.presentation.dialogs.alert

import com.markoid.parky.core.presentation.enums.AlertType

typealias OnPositiveListener = AppDialogInterface.() -> Unit
typealias OnNegativeListener = AppDialogInterface.() -> Unit

interface AppDialogInterface {
    var isDialogCancelable: Boolean
    val isDialogShowing: Boolean
    var message: String
    var type: AlertType
    var positiveListener: OnPositiveListener?
    var negativeListener: OnNegativeListener?
    fun close()
}
