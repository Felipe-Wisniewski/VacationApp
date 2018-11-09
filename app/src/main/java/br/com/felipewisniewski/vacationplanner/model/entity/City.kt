package br.com.felipewisniewski.vacationplanner.model.entity

data class City(
        val woeid: String,
        val district: String,
        val province: String,
        val state_acronym: String,
        val country: String
)