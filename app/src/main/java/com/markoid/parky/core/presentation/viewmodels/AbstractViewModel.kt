package com.markoid.parky.core.presentation.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

open class AbstractViewModel : ViewModel() {

    private val lifeCycleState = MutableSharedFlow<Lifecycle.State>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val lifecycleObserver = object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            lifeCycleState.tryEmit(event.targetState)
            if (event.targetState == Lifecycle.State.DESTROYED) {
                source.lifecycle.removeObserver(this)
            }
        }
    }

    fun startObservingLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
    }

    protected fun <T> Flow<T>.whenAtLeast(requiredState: Lifecycle.State): Flow<T> {
        return lifeCycleState.map { state -> state.isAtLeast(requiredState) }
            .distinctUntilChanged()
            .flatMapLatest { if (it) this else emptyFlow() }
    }
}
