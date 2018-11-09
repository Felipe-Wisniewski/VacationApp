package br.com.felipewisniewski.vacationplanner.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.felipewisniewski.vacationplanner.R

/**
 *  Activity main. Used for acessing fragment main
 **/
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.containerMain, MainFragment.newInstance())
                    .commitNow()
        }
    }

}
