package com.markoid.parky.core.presentation.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.whenStarted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun LifecycleCoroutineScope.launchWhenStarted(
    context: CoroutineContext,
    lifecycle: Lifecycle,
    block: suspend CoroutineScope.() -> Unit
): Job = launch(context) { lifecycle.whenStarted(block) }

fun LifecycleCoroutineScope.launchWhenStartedCatching(
    lifecycle: Lifecycle,
    block: suspend CoroutineScope.() -> Unit,
    errorBlock: suspend (Throwable) -> Unit
): Job = launch {
    runCatching {
        lifecycle.whenStarted(block)
    }.onFailure {
        it.printStackTrace()
        errorBlock.invoke(it)
    }
}
