package fr.ekito.myweatherapp.view

/**
 * Created by loic - LinkValue on 24/04/2018.
 */
open class State {
    /**
     * Generic Loading State
     */
    object LoadingState : State()

    /**
     * Generic Error state
     * @param error - caught error
     */
    data class ErrorState(val error: Throwable) : State()
}