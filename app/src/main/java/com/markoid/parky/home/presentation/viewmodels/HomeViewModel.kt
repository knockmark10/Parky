package com.markoid.parky.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markoid.parky.core.presentation.dispatchers.CoroutineDispatcherProvider
import com.markoid.parky.core.presentation.observers.UseCaseObserver
import com.markoid.parky.position.data.entities.PositionEntity
import com.markoid.parky.position.domain.usecases.GetCurrentLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    getCurrentLocationUseCase: GetCurrentLocationUseCase,
    dispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val currentLocationObserver =
        UseCaseObserver(getCurrentLocationUseCase, dispatcherProvider, viewModelScope)

    fun getCurrentLocation(): UseCaseObserver<PositionEntity, Unit> =
        currentLocationObserver.execute(Unit)

    override fun onCleared() {
        super.onCleared()
        currentLocationObserver.dispose()
    }
}
