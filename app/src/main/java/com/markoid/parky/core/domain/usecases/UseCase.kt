package com.markoid.parky.core.domain.usecases

import com.markoid.parky.core.presentation.states.LoadingState
import com.markoid.parky.core.presentation.states.State
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Base class for the use cases. All of them need to extend from this to ensure error handling.
 */
abstract class UseCase<Result, Request> {

    /**
     * Holds the communication flow required to communicate between use case and observer.
     */
    private var sender: MutableSharedFlow<State<Result>> = MutableSharedFlow()

    /**
     * Receiving channel that observer can subscribe to in order to collect the different states
     * of the use case.
     */
    internal val receiver: SharedFlow<State<Result>>
        get() = sender.asSharedFlow()

    /**
     * Method that needs to be implemented in all the use cases. It will perform the main and
     * only purpose of the use case.
     */
    abstract suspend fun onExecute(request: Request): Result

    /**
     * It gets called by [UseCaseObserver] to emit different states that can be collected by the
     * observer.
     */
    internal suspend operator fun invoke(request: Request) {
        try {
            // Show loading when the use case execution starts
            sender.emit(State.Loading(LoadingState.Show))
            // Execute the use case and store the return value
            val result = onExecute(request)
            // Hide loading after use case execution ends
            sender.emit(State.Loading(LoadingState.Dismiss))
            // Send the result from the use case
            sender.emit(State.Success(result))
        } catch (t: Throwable) {
            // Dismiss the pending loading
            sender.emit(State.Loading(LoadingState.Dismiss))
            // Send the error through the flow.
            // Use the return value from #onError method
            sender.emit(State.Failure(onError(t)))
        } finally {
            // Restart sender object to prevent it from caching past values
            this.sender = MutableSharedFlow()
        }
    }

    /**
     * Get the initial value to display the loading message.
     *
     * @return Loading message to display
     */
    open fun onInitialLoadingMessage(): String = ""

    /**
     * This will allow to customize the errors caught in any use case that requires it.
     * By default it will return the same error that was inputted.
     *
     * @param error - The error caught
     * @return The same error, or custom error caught by each use case
     */
    open suspend fun onError(error: Throwable): Throwable = error
}
