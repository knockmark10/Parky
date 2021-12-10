package com.markoid.parky.core.domain.exceptions

import java.io.IOException

class EmptyErrorBodyException(
    private val errorMessage: String = "Error body is null."
) : IOException(errorMessage) {

    override fun getLocalizedMessage(): String = this.errorMessage
}
