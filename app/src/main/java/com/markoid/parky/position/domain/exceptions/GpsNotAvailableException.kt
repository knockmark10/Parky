package com.markoid.parky.position.domain.exceptions

import java.io.IOException

class GpsNotAvailableException(
    private val errorMessage: String = "Location permission is not granted. Therefore, the GPS is not available."
) : IOException(errorMessage) {

    override fun getLocalizedMessage(): String = this.errorMessage
}
