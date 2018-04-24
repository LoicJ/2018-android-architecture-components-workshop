package fr.ekito.myweatherapp.view.weather

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import fr.ekito.myweatherapp.data.repository.WeatherRepository
import fr.ekito.myweatherapp.domain.DailyForecastModel
import fr.ekito.myweatherapp.util.mvp.RxPresenter
import fr.ekito.myweatherapp.util.mvvm.RxViewModel
import fr.ekito.myweatherapp.util.rx.SchedulerProvider
import fr.ekito.myweatherapp.util.rx.with
import fr.ekito.myweatherapp.view.State


class WeatherViewModel(
        private val weatherRepository: WeatherRepository,
        private val schedulerProvider: SchedulerProvider
) : RxViewModel() {

    private val mStates = MutableLiveData<State>()
    val states: LiveData<State>
        get() = mStates

    fun loadNewLocation(location: String) {
//        launch {
//            weatherRepository.getWeather(location).toCompletable()
//                    .with(schedulerProvider)
//                    .subscribe(
//                            { view?.showLocationSearchSucceed(location) },
//                            { error -> view?.showLocationSearchFailed(location, error) })
//        }
    }

    fun getWeather() {
        mStates.value = State.LoadingState
        launch {
            weatherRepository.getWeather()
                    .map { it.first() }
                    .with(schedulerProvider)
                    .subscribe(
                            { weather -> mStates.value = WeatherListState(weather.location, weather, listOf(weather)) },
                            { error -> mStates.value = State.ErrorState(error) })
        }
    }

    data class WeatherListState(
            val location: String,
            val first: DailyForecastModel,
            val lasts: List<DailyForecastModel>
    ) : State() {
        companion object {
            fun from(list: List<DailyForecastModel>): WeatherListState {
                return if (list.isEmpty()) error("weather list should not be empty")
                else {
                    val first = list.first()
                    val location = first.location
                    WeatherListState(location, first, list.takeLast(list.size - 1))
                }
            }
        }
    }
}