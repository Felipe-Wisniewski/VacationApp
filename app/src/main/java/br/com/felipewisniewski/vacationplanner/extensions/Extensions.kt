package br.com.felipewisniewski.vacationplanner.extensions

fun String.MonthNumberToWord() : String {
    var month = ""
    when(this){
        "01" -> month = "January"
        "02" -> month = "February"
        "03" -> month = "March"
        "04" -> month = "April"
        "05" -> month = "May"
        "06" -> month = "June"
        "07" -> month = "July"
        "08" -> month = "August"
        "09" -> month = "September"
        "10" -> month = "October"
        "11" -> month = "November"
        "12" -> month = "December"
    }
    return month
}