package fr.ekito.myweatherapp.view.splash

import android.arch.lifecycle.LiveData
import fr.ekito.myweatherapp.data.repository.WeatherRepository
import fr.ekito.myweatherapp.util.mvp.RxPresenter
import fr.ekito.myweatherapp.util.mvvm.RxViewModel
import fr.ekito.myweatherapp.util.mvvm.SingleLiveEvent
import fr.ekito.myweatherapp.util.rx.SchedulerProvider
import fr.ekito.myweatherapp.util.rx.with
import fr.ekito.myweatherapp.view.Event

class SplashViewModel(
        private val weatherRepository: WeatherRepository,
        private val schedulerProvider: SchedulerProvider
) : RxViewModel() {

    private val mEvents = SingleLiveEvent<Event>()
    val events: LiveData<Event>
        get() = mEvents

    fun getLastWeather() {
//        view?.showIsLoading()
        launch {
            mEvents.value = Event.LoadingEvent
            weatherRepository.getWeather().with(schedulerProvider)
                    .toCompletable()
                    .subscribe({
                        mEvents.value = Event.SuccessEvent
                    },
                            { error -> mEvents.value = Event.ErrorEvent(error) })
        }
    }
}