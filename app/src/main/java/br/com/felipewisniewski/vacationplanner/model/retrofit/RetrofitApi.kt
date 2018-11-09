package br.com.felipewisniewski.vacationplanner.model.retrofit

import br.com.felipewisniewski.vacationplanner.model.entity.City
import br.com.felipewisniewski.vacationplanner.model.entity.SeasonWeb
import br.com.felipewisniewski.vacationplanner.model.entity.Weather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit database request.
 */
interface RetrofitApi{

    /**
     * Get a list of cities.
     * @return the list of cities with a specific id, city, province, state and country.
     */
    @GET("cities/")
    fun listOfCities() : Observable<MutableList<City>>

    /**
     * Get a list of weather.
     * @return the list of weather with a specific id.
     */
    @GET("weather/")
    fun listOfWeather() : Observable<MutableList<Weather>>

    /**
     * Get a list of weather from a specific city and year.
     * @param woeid city ID.
     * @param year chosen year.
     * @return the list of weather.
     */
    @GET("cities/{id}/year/{year}")
    fun listOfSeasons(@Path("id") woeid: String,
                      @Path("year") year: String) : Observable<MutableList<SeasonWeb>>

}