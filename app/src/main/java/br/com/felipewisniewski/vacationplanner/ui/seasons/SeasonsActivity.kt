package br.com.felipewisniewski.vacationplanner.ui.seasons

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.felipewisniewski.vacationplanner.R

/**
 *  Activity seasons. Used for acessing fragment seasons.
 **/
class SeasonsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seasons)

        if (savedInstanceState == null) {
            val bundle= intent?.extras?.getBundle("choices")
            val seasonsFragment = SeasonsFragment.newInstance()
            seasonsFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                    .replace(R.id.containerSeasons, seasonsFragment)
                    .commitNow()
        }
    }
}
