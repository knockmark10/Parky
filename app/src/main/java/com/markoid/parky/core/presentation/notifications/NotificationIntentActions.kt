package com.markoid.parky.core.presentation.notifications

enum class NotificationIntentActions(val action: String) {
    ACTION_ADD_PARKING("action_add_parking"),
    ACTION_USER_LOCATION("action_user_location"),
    ACTION_SETTINGS("action_settings");

    companion object {
        fun getTypeByAction(action: String): NotificationIntentActions? =
            values().firstOrNull { it.action == action }
    }
}
