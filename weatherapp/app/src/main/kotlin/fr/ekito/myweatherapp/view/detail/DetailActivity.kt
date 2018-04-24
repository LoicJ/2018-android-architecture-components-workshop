package fr.ekito.myweatherapp.view.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import fr.ekito.myweatherapp.R
import fr.ekito.myweatherapp.domain.DailyForecastModel
import fr.ekito.myweatherapp.domain.getColorFromCode
import fr.ekito.myweatherapp.util.ext.argument
import fr.ekito.myweatherapp.view.IntentArguments.ARG_WEATHER_ITEM_ID
import fr.ekito.myweatherapp.view.State
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

/**
 * Weather Detail View
 */
class DetailActivity : AppCompatActivity() {

    // Detail id passed by argument
    private val detailId by argument<String>(ARG_WEATHER_ITEM_ID)

    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        detailViewModel.apply {
            weatherRepository = get()
            schedulerProvider = get()
        }
        detailViewModel.states.observe(this, Observer { state ->
            state?.let {
                when (state) {
                    is State.ErrorState -> showError(state.error)
                    is DetailViewModel.WeatherDetailState -> showDetail(state.weather)
                }
            }
        })

        detailViewModel.getDetail(detailId)
    }


    fun showError(error: Throwable) {
        Snackbar.make(weatherItem, getString(R.string.loading_error) + " $error", Snackbar.LENGTH_LONG).show()
    }

    fun showDetail(weather: DailyForecastModel) {
        weatherIcon.text = weather.icon
        weatherDay.text = weather.day
        weatherText.text = weather.fullText
        weatherWindText.text = weather.wind.toString()
        weatherTempText.text = weather.temperature.toString()
        weatherHumidityText.text = weather.humidity.toString()
        weatherItem.background.setTint(getColorFromCode(weather))
        // Set back on background click
        weatherItem.setOnClickListener {
            onBackPressed()
        }
    }
}
