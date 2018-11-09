package br.com.felipewisniewski.vacationplanner.ui.seasons

import android.arch.lifecycle.ViewModel;
import br.com.felipewisniewski.vacationplanner.model.entity.Season
import br.com.felipewisniewski.vacationplanner.model.retrofit.Retrofit
import io.reactivex.Observable

/**
 * View Model for the {@link SeasonsFragment}
 */
class SeasonsViewModel : ViewModel() {

    private val service = Retrofit.getRetrofit()

    /**
     * Get a list of possible seasons.
     * @param cityId the ID of the chosen city.
     * @param yearVacation the year of the vacations.
     * @return a {@link Observable} of the Season.
     */
    fun getSeasons(cityId: String, yearVacation: String) : Observable<Season>  {
        return service.listOfSeasons(cityId, yearVacation)
                .flatMap { seasons -> Observable.fromIterable(seasons) }
                .map { season -> Season(season.date.drop(5).replace("-",""),
                        season.weather) }
    }
}
