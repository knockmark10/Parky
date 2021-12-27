package com.markoid.parky.core.presentation.observers

import com.markoid.parky.core.data.enums.DataState
import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.core.presentation.dispatchers.CoroutineDispatcherProvider
import com.markoid.parky.core.presentation.states.LoadingState
import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

class UseCaseFlowObserver<Result, Request>(
    private val baseUseCase: UseCase<Result, Request>,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val coroutineScope: CoroutineScope
) {

    private val parentJob: CompletableJob = SupervisorJob()

    private val useCaseInvocationContext: CoroutineContext
        get() = parentJob + dispatcherProvider.io

    private val sendSharedFlow: MutableSharedFlow<DataState<Result>> = MutableSharedFlow()

    private val loadingSharedFlow: MutableSharedFlow<LoadingState> = MutableSharedFlow()

    /**
     * In charge of executing the use case, and handling the state emitted by it.
     */
    fun execute(request: Request): UseCaseFlowObserver<Result, Request> {
        // Launch one job to collect the events emitted by sender
        coroutineScope.launch(dispatcherProvider.io) {
            // Collect the events from the use case
            baseUseCase.receiver.collect {
                // Process the state handling inside main thread
                withContext(dispatcherProvider.main) {
                    // Handle the events emitted by the use case
                    it.handleState(
                        successBlock = { state -> sendSharedFlow.emit(DataState.Data(state)) },
                        failureBlock = { throwable ->
                            val error = throwable.localizedMessage ?: throwable.message ?: "Error"
                            sendSharedFlow.emit(DataState.Error(error))
                        },
                        loadingBlock = { loading -> loadingSharedFlow.emit(loading) }
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
    fun getResult(): SharedFlow<DataState<Result>> = sendSharedFlow.asSharedFlow()

    /**
     * Provides notification for the loading states
     */
    fun getLoading(): SharedFlow<LoadingState> = loadingSharedFlow.asSharedFlow()

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
