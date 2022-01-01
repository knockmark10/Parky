package com.markoid.parky.core.presentation.providers

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityProvider @Inject constructor() : Application.ActivityLifecycleCallbacks {

    private val activityFlowStateFlow: MutableStateFlow<WeakReference<ComponentActivity>> =
        MutableStateFlow(WeakReference<ComponentActivity>(null))

    private val activityFlow: Flow<ComponentActivity> = activityFlowStateFlow.asSharedFlow()
        .distinctUntilChanged { old, new -> old.get() == new.get() }
        .filter { it.get() != null }
        .map { it.get()!! }

    val currentActivity: ComponentActivity?
        get() = activityFlowStateFlow.value.get()

    private fun startComponent(activity: Activity) {
        (activity as? ComponentActivity)?.let {
            activityFlowStateFlow.value = WeakReference(it)
        }
    }

    fun init(application: Application) = application.registerActivityLifecycleCallbacks(this)

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        startComponent(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        startComponent(activity)
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}
