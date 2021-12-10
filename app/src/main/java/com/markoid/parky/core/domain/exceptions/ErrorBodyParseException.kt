package com.markoid.parky.core.domain.exceptions

import java.io.IOException

class ErrorBodyParseException(
    private val errorMessage: String = "Unable to parse error body."
) : IOException(errorMessage) {

    override fun getLocalizedMessage(): String = this.errorMessage
}
