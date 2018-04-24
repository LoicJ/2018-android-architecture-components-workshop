package fr.ekito.myweatherapp.view.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import fr.ekito.myweatherapp.data.repository.WeatherRepository
import fr.ekito.myweatherapp.domain.DailyForecastModel
import fr.ekito.myweatherapp.util.mvp.RxPresenter
import fr.ekito.myweatherapp.util.mvvm.RxViewModel
import fr.ekito.myweatherapp.util.rx.SchedulerProvider
import fr.ekito.myweatherapp.util.rx.with
import fr.ekito.myweatherapp.view.State

class DetailViewModel : RxViewModel() {

    lateinit var weatherRepository: WeatherRepository
    lateinit var schedulerProvider: SchedulerProvider

    private val mStates = MutableLiveData<State>()
    val states: LiveData<State>
        get() = mStates


    fun getDetail(id: String) {
        mStates.value = State.LoadingState
        launch {
            weatherRepository.getWeatherDetail(id).with(schedulerProvider).subscribe(
                    { detail ->
                        mStates.value = WeatherDetailState(detail)

                    }, { error -> mStates.value = State.ErrorState(error) })
        }
    }

    data class WeatherDetailState(val weather: DailyForecastModel) : State()
}