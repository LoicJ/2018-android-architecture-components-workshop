package fr.ekito.myweatherapp.view

/**
 * Created by loic - LinkValue on 24/04/2018.
 */
open class Event {

    object LoadingEvent : Event()
    object SuccessEvent : Event()

    data class ErrorEvent(val error: Throwable) : Event()
}