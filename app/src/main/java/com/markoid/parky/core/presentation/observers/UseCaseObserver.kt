package com.markoid.parky.core.presentation.observers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.core.presentation.dispatchers.CoroutineDispatcherProvider
import com.markoid.parky.core.presentation.states.LoadingState
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Class that will handle the use case execution
 */
class UseCaseObserver<Result, Request>(
    private val baseUseCase: UseCase<Result, Request>,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val coroutineScope: CoroutineScope
) {

    private val parentJob: CompletableJob = SupervisorJob()

    private val useCaseInvocationContext: CoroutineContext
        get() = parentJob + dispatcherProvider.io

    private val senderLiveData: MutableLiveData<DataState<Result>> = MutableLiveData()

    private val loadingLiveData: MutableLiveData<LoadingState> = MutableLiveData()

    /**
     * In charge of executing the use case, and handling the state emitted by it.
     */
    fun execute(request: Request): UseCaseObserver<Result, Request> {
        // Launch one job to collect the events emitted by sender
        coroutineScope.launch(dispatcherProvider.io) {
            // Collect the events from the use case
            baseUseCase.receiver.collect {
                // Process the state handling inside main thread
                withContext(dispatcherProvider.main) {
                    // Handle the events emitted by the use case
                    it.handleState(
                        successBlock = { state -> senderLiveData.value = DataState.Data(state) },
                        failureBlock = { throwable ->
                            val error = throwable.localizedMessage ?: throwable.message ?: "Error"
                            senderLiveData.postValue(DataState.Error(error))
                        },
                        loadingBlock = { loading -> loadingLiveData.postValue(loading) }
                    )
                }
            }
        }
        // Launch another job to execute main use case with request provided
        coroutineScope.launch(useCaseInvocationContext) { baseUseCase.invoke(request) }
        return this
    }

    /**
     * Provides notifications for the data flow.
     */
    fun getResult(): LiveData<DataState<Result>> = senderLiveData

    /**
     * Provides notification for the loading states
     */
    fun getLoading(): LiveData<LoadingState> = loadingLiveData

    /**
     * Stop the use case execution when it's no longer needed.
     */
    fun dispose(): Boolean = if (parentJob.isCancelled.not()) {
        parentJob.cancel()
        true
    } else {
        false
    }
}
