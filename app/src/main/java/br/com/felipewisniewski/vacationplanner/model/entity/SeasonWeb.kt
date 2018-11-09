package br.com.felipewisniewski.vacationplanner.model.entity

data class SeasonWeb(
        val date: String,
        val temperature: Temperature,
        val weather: String,
        val woeid: String
)

data class Temperature(
        val max: Int,
        val min: Int,
        val unit: String
)

