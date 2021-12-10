package com.markoid.parky.core.data.handlers

import com.markoid.parky.core.data.entities.IDataError
import com.markoid.parky.core.domain.exceptions.EmptyErrorBodyException
import com.markoid.parky.core.domain.exceptions.EmptyResponseBodyException
import com.markoid.parky.core.domain.exceptions.ErrorBodyParseException
import com.markoid.parky.position.presentation.annotations.BigDataCloudServer
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class ResponseHandler @Inject constructor(@BigDataCloudServer val retrofit: Retrofit) {

    inline fun <Result, reified Error : IDataError> handleResponseWithCustomError(response: Response<Result>): Result {
        if (response.isSuccessful) {
            return response.body() ?: throw EmptyResponseBodyException()
        } else {
            val errorBody: ResponseBody? = response.errorBody()
            throw if (errorBody != null) {
                val converter: Converter<ResponseBody, IDataError> =
                    retrofit.responseBodyConverter(Error::class.java, arrayOf())
                val apiError: IDataError? = converter.convert(errorBody)
                if (apiError != null) {
                    Throwable(apiError.message)
                } else {
                    ErrorBodyParseException()
                }
            } else {
                EmptyErrorBodyException()
            }
        }
    }

    fun <Result> handleResponse(response: Response<Result>): Result {
        if (response.isSuccessful) {
            return response.body() ?: throw EmptyResponseBodyException()
        } else {
            val errorBody: ResponseBody? = response.errorBody()
            throw if (errorBody != null) {
                val converter: Converter<ResponseBody, IDataError> =
                    retrofit.responseBodyConverter(IDataError::class.java, arrayOf())
                val apiError: IDataError? = converter.convert(errorBody)
                if (apiError != null) {
                    Throwable(apiError.message)
                } else {
                    ErrorBodyParseException()
                }
            } else {
                EmptyErrorBodyException()
            }
        }
    }
}
