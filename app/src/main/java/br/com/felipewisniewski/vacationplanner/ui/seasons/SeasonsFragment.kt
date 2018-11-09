package br.com.felipewisniewski.vacationplanner.ui.seasons

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import br.com.felipewisniewski.vacationplanner.R
import br.com.felipewisniewski.vacationplanner.model.entity.Search
import br.com.felipewisniewski.vacationplanner.model.entity.Season
import br.com.felipewisniewski.vacationplanner.model.entity.Selected
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.seasons_fragment.*

/**
 * Output data fragment.
 */
class SeasonsFragment : Fragment() {

    companion object { fun newInstance() = SeasonsFragment() }

    private lateinit var viewModel: SeasonsViewModel
    private val disposable = CompositeDisposable()

    private val seasonsAdapter: SeasonsAdapter by lazy { SeasonsAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.seasons_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SeasonsViewModel::class.java)

        val bundle = this.activity?.intent?.getBundleExtra("choices")
        val choices = bundle?.getSerializable("choices") as Search

        loadSeasons(choices)

        val viewManager = LinearLayoutManager(this.context)
        recycleSeasons.layoutManager = viewManager
        recycleSeasons.adapter = seasonsAdapter
    }

    /**
     * Load the list of possible seasons from the database and get notified when an error occurs.
     */
    private fun loadSeasons(choices: Search) {
        val listOfSeasons = arrayListOf<Season>()
        disposable.add(viewModel.getSeasons(choices.cityId, choices.yearVacation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { season ->
                            listOfSeasons.add(season) },
                        { e ->
                            Log.e("Err", "Error subscribe getSeasons: $e")
                            alertToast(resources.getString(R.string.error_load_data)) },
                        { loadChoices(listOfSeasons, choices) }
                ))
    }

    /**
     * Sroll through the list of seasons and sort by size groups equal to vacation days.
     */
    private fun loadChoices(listOfSeasons: ArrayList<Season>, choices: Search) {
        val listRangeOfDays = arrayListOf<MutableList<Season>>()

        Observable.fromIterable(listOfSeasons)
                .buffer(choices.daysVacation, 1)
                .flatMap { buffer -> Observable.just(buffer) }
                .map { listBuffered -> listRangeOfDays.add(listBuffered) }
                .subscribe()

        val selected = arrayListOf<Selected>()
        var acc = 0

        for (bufferDays in listRangeOfDays){
            for (day in bufferDays){
                if (choices.chosenWeathers.contains(day.season)) {
                    acc++
                }
            }
            if (acc != 0){
                val select = Selected(acc, bufferDays.first().seasonId, bufferDays.last().seasonId)
                selected.add(select)
            }
            acc = 0
        }
        seasonsAdapter.add(selected)
    }

    private fun alertToast(msg: String) {
        Toast.makeText(this.context, msg, Toast.LENGTH_LONG).show()
    }

}
