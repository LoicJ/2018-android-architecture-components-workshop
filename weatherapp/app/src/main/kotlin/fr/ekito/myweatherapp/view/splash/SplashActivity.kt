package fr.ekito.myweatherapp.view.splash

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import fr.ekito.myweatherapp.R
import fr.ekito.myweatherapp.view.Event
import fr.ekito.myweatherapp.view.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

/**
 * Search Weather View
 */
class SplashActivity : AppCompatActivity() {

    // Presenter
//    override val presenter: SplashContract.Presenter by inject()
    private val splashViewModel: SplashViewModel by viewModel<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashViewModel.events.observe(this, Observer { events ->
            events?.let {
                when (events) {
                    is Event.LoadingEvent -> showIsLoading()
                    is Event.SuccessEvent -> showIsLoaded()
                    is Event.ErrorEvent -> showError(events.error)
                }
            }
        })

        splashViewModel.getLastWeather()
    }


    fun showIsLoading() {
        val animation =
                AnimationUtils.loadAnimation(applicationContext, R.anim.infinite_blinking_animation)
        splashIcon.startAnimation(animation)
    }

    fun showIsLoaded() {
        startActivity(intentFor<WeatherActivity>().clearTop().clearTask().newTask())
    }

    fun showError(error: Throwable) {
        splashIcon.visibility = View.GONE
        splashIconFail.visibility = View.VISIBLE
        Snackbar.make(
                splash,
                getString(R.string.loading_error) + " $error",
                Snackbar.LENGTH_INDEFINITE
        )
                .setAction(R.string.retry, {
                    splashViewModel.getLastWeather()
                })
                .show()
    }
}
