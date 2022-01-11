package com.markoid.parky.core.domain.exceptions

import java.io.IOException

class ServerException(
    private val errorMessage: String = "Server is unavailable. Please retry later."
) : IOException() {

    override fun getLocalizedMessage(): String? = this.errorMessage
}
