package com.markoid.parky.core.presentation.states

typealias OnSuccessBlocking<T> = suspend (data: T) -> Unit
typealias OnErrorBlocking = suspend (error: Throwable) -> Unit
typealias OnLoadingBlocking = suspend (state: LoadingState) -> Unit

sealed class State<out T> {

    class Success<out T>(val successData: T) : State<T>()
    class Failure(val errorData: Throwable) : State<Nothing>()
    class Loading(val state: LoadingState) : State<Nothing>()

    suspend fun handleState(
        successBlock: OnSuccessBlocking<T> = {},
        failureBlock: OnErrorBlocking = {},
        loadingBlock: OnLoadingBlocking = {}
    ) {
        when (this@State) {
            is Success -> successBlock(successData)
            is Failure -> failureBlock(errorData)
            is Loading -> loadingBlock(state)
        }
    }
}

sealed class LoadingState {
    object Show : LoadingState()
    object Dismiss : LoadingState()
}

fun <T> State<T>.getName(): String = when (this) {
    is State.Failure -> errorData.localizedMessage ?: errorData.message ?: "Unknown error"
    is State.Loading -> when (state) {
        LoadingState.Dismiss -> "Loading Dismiss"
        is LoadingState.Show -> "Loading Show"
    }
    is State.Success -> successData.toString()
}
