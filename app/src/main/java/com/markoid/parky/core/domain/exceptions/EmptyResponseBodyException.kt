package com.markoid.parky.core.domain.exceptions

import java.io.IOException

class EmptyResponseBodyException(
    private val errorMessage: String = "Response body is null."
) : IOException(errorMessage) {

    override fun getLocalizedMessage(): String? = this.errorMessage
}
