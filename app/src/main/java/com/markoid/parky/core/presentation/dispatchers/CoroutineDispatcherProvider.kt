package com.markoid.parky.core.presentation.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {
    val computation: CoroutineDispatcher
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
}
