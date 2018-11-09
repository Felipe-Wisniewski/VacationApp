package br.com.felipewisniewski.vacationplanner.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.felipewisniewski.vacationplanner.R
import br.com.felipewisniewski.vacationplanner.model.entity.City
import br.com.felipewisniewski.vacationplanner.model.entity.Search
import br.com.felipewisniewski.vacationplanner.ui.seasons.SeasonsActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*

/**
 * Input data fragment.
 */
class MainFragment : Fragment() {

    companion object { fun newInstance() = MainFragment() }

    private lateinit var viewModel: MainViewModel
    private val disposable = CompositeDisposable()

    private var listOfCities = mutableListOf<City>()
    private var cities = arrayListOf<String>()

    private var weatherNames = arrayListOf<String>()
    private var seasonChoices = arrayListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        loadCities()
        loadWeathers()

        tv_destination.setOnClickListener { selectCity() }

        tv_days.setOnClickListener { selectDay() }

        tv_year.setOnClickListener {
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val years = arrayOf("$year", "${year + 1}", "${year + 2}")
            selectYear(years)
        }

        tv_weather.setOnClickListener { selectWeather() }

        btn_search.setOnClickListener { searchDaysVacation() }
    }

    private fun selectCity(){
        AlertDialog.Builder(this.context!!)
                .setTitle(resources.getString(R.string.adialog_destination))
                .setItems(cities.toTypedArray()) {
                    dialog, which ->
                        tv_destination.text = cities[which]
                        dialog.dismiss() }
                .show()
    }

    private fun selectDay() {
        AlertDialog.Builder(this.context!!)
                .setTitle(resources.getString(R.string.adialog_day))
                .setItems(resources.getStringArray(R.array.days)) {
                    dialog, which ->
                        tv_days.text = resources.getStringArray(R.array.days)[which]
                        dialog.dismiss() }
                .show()
    }

    private fun selectYear(year: Array<String>) {
        AlertDialog.Builder(this.context!!)
                .setTitle(resources.getString(R.string.adialog_year))
                .setItems(year) {
                    dialog, which ->
                        tv_year.text = year[which]
                        dialog.dismiss() }
                .show()
    }

    private fun selectWeather() {
        val checked = BooleanArray(weatherNames.size) { false }
        AlertDialog.Builder(this.context!!)
                .setTitle(resources.getString(R.string.adialog_weather))
                .setMultiChoiceItems(weatherNames.toTypedArray(), checked) {
                    _, which, isChecked ->
                        checked[which] = isChecked
                }
                .setPositiveButton("OK") {
                    _, _ ->
                        val seasonSelected = arrayListOf<String>()
                        for (i in weatherNames.indices) {
                            if (checked[i])
                                seasonSelected.add(weatherNames[i])
                        }
                        if (seasonSelected.isNotEmpty()) {
                            seasonChoices.addAll(seasonSelected)
                            tv_weather.text = getString(R.string.tv_weather_selected)
                        }
                }
                .show()
    }

    private fun searchDaysVacation() {
        if (validateForm()) {
            var cityId = ""
            for (i in listOfCities.indices) {
                if (tv_destination.text == listOfCities[i].district)
                    cityId = listOfCities[i].woeid
            }
            val days = tv_days.text.toString()
            val year = tv_year.text.toString()

            val bundle = Bundle()
            bundle.putSerializable("choices", Search(cityId, days.toInt(), year, seasonChoices))
            val intent = Intent(this.context, SeasonsActivity::class.java)
            intent.putExtra("choices", bundle)
            disposable.clear()
            startActivity(intent)
        }
    }

    /**
     * Load the list of cities from the database and get notified when an error occurs.
     */
    private fun loadCities() {
        disposable.add(viewModel.getCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { city -> listOfCities.add(city)
                            cities.add(city.district) },
                        { e ->
                            Log.e("Err", "Error subscribe getCity: $e")
                            alertToast(resources.getString(R.string.error_load_data)) }))
    }

    /**
     * Load the list of weather from the database and get notified when an error occurs.
     */
    private fun loadWeathers() {
        disposable.add(viewModel.getWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { weather -> weatherNames.add(weather.name) },
                        { e ->
                            Log.e("Err", "Error subscribe getWeather: $e")
                            alertToast(resources.getString(R.string.error_load_data)) }))
    }

    private fun validateForm() : Boolean {
        var validated = 0

        if (tv_destination.text == resources.getString(R.string.text_destination)) {
            textLayoutDestination.isErrorEnabled = true
            textLayoutDestination.error = resources.getString(R.string.error_form_destination)
            validated++
        } else {
            textLayoutDestination.isErrorEnabled = false
        }

        if (tv_days.text == resources.getString(R.string.text_days) || tv_year.text == resources.getString(R.string.text_year)) {
            textLayoutDays.isErrorEnabled = true
            textLayoutDays.error = resources.getString(R.string.error_form_days_year)
            validated++
        } else {
            textLayoutDays.isErrorEnabled = false
        }

        if (tv_weather.text == resources.getString(R.string.text_weather)) {
            textLayoutWeather.isErrorEnabled = true
            textLayoutWeather.error = resources.getString(R.string.error_form_weather)
            validated++
        } else {
            textLayoutWeather.isErrorEnabled = false
        }
        return validated == 0
    }

    private fun alertToast(msg: String) {
        Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show()
    }
}
