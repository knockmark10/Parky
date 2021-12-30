package com.markoid.parky.settings.domain.responses

import androidx.annotation.StringRes

sealed class ExclusionZoneValidationStatus {
    object Success : ExclusionZoneValidationStatus()

    sealed class Failure(val message: String) {
        data class InvalidLocation(@StringRes val resId: Int) : ExclusionZoneValidationStatus()
        data class MissingName(@StringRes val resId: Int) : ExclusionZoneValidationStatus()
        data class WrongColor(@StringRes val resId: Int) : ExclusionZoneValidationStatus()
    }
}
