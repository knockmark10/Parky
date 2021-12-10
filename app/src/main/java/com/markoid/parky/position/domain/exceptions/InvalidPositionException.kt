package com.markoid.parky.position.domain.exceptions

import java.io.IOException

class InvalidPositionException(
    private val errorMessage: String = "Gps could not get a valid position (0,0)."
) : IOException(errorMessage) {

    override fun getLocalizedMessage(): String = this.errorMessage
}
