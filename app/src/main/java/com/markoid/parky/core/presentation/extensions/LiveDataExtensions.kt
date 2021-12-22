package com.markoid.parky.core.presentation.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> LiveData<T>.subscribe(lifecycleOwner: LifecycleOwner, block: (T) -> Unit) {
    if (!this.hasObservers()) {
        this.observe(lifecycleOwner, { block(it) })
    }
}

fun <T> LiveData<T>.react(lifecycleOwner: LifecycleOwner, block: T.() -> Unit) {
    if (!this.hasObservers()) {
        this.observe(lifecycleOwner, { block(it) })
    }
}

fun <T> MutableLiveData<T>.subscribe(lifecycleOwner: LifecycleOwner, blocks: (T) -> Unit) {
    if (!this.hasObservers()) {
        this.observe(lifecycleOwner, { blocks(it) })
    }
}
