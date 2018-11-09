package br.com.felipewisniewski.vacationplanner.ui.main

import android.arch.lifecycle.ViewModel
import br.com.felipewisniewski.vacationplanner.model.entity.City
import br.com.felipewisniewski.vacationplanner.model.entity.Season
import br.com.felipewisniewski.vacationplanner.model.entity.Weather
import br.com.felipewisniewski.vacationplanner.model.retrofit.Retrofit
import io.reactivex.Observable

/**
 * View Model for the {@link MainFragment}
 */
class MainViewModel : ViewModel() {

    private val service = Retrofit.getRetrofit()

    /**
     * Get the City of the cities list.
     * @return a {@link Observable} of the City.
     */
    fun getCity() : Observable<City> {
        return service.listOfCities()
                .flatMapIterable { cities -> cities }
                .map { city -> City(city.woeid, city.district, city.province, city.state_acronym, city.country)}
    }

    /**
     * Get the Weather of the weather list.
     * @return a {@link Observable} of the Weather.
     */
    fun getWeather() : Observable<Weather> {
        return service.listOfWeather()
                .flatMapIterable { weathers -> weathers }
                .map { weather -> Weather(weather.id, weather.name)}
    }

}



