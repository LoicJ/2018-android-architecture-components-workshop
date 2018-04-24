package fr.ekito.myweatherapp.view.weather

import android.arch.lifecycle.Observer
import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import fr.ekito.myweatherapp.R
import fr.ekito.myweatherapp.R.id.*
import fr.ekito.myweatherapp.view.Event
import fr.ekito.myweatherapp.view.State
import fr.ekito.myweatherapp.view.detail.DetailViewModel
import kotlinx.android.synthetic.main.activity_result.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.koin.android.architecture.ext.viewModel

/**
 * Weather Result View
 */
class WeatherActivity : AppCompatActivity() {

    val TAG = this::class.java.simpleName

    private val weatherViewModel: WeatherViewModel by viewModel<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val weatherTitleFragment = WeatherHeaderFragment()
        val resultListFragment = WeatherListFragment()

        weatherViewModel.states.observe(this, Observer { state ->
            state?.let {
                when (state) {
                    is State.ErrorState -> showError(state.error)
//                        is DetailViewModel.WeatherDetailState -> showDetail(state.weather)
                }
            }
        })

        weatherViewModel.getWeather()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.weather_title, weatherTitleFragment)
                .commit()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.weather_list, resultListFragment)
                .commit()
    }

    private fun showError(error: Throwable) {
        Log.e(TAG, "error $error while displaying weather")
        weather_views.visibility = View.GONE
        weather_error.visibility = View.VISIBLE
        Snackbar.make(
                weather_result,
                "WeatherActivity got error : $error",
                Snackbar.LENGTH_INDEFINITE
        )
                .setAction(R.string.retry, {
                    startActivity(intentFor<WeatherActivity>().clearTop().clearTask().newTask())
                })
                .show()
    }
}
