package br.com.felipewisniewski.vacationplanner.model.entity

import java.io.Serializable
import java.util.*

data class Search(
        val cityId: String,
        val daysVacation: Int,
        val yearVacation: String,
        val chosenWeathers: ArrayList<String>
) : Serializable

data class Selected(
        val count: Int,
        val dateFrom: String,
        val dateTo: String
)