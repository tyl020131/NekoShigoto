package com.example.nekoshigoto

data class Vacancy(
    var vacancyid : String = "",
    val image:String="",
    val position : String="",
    val field : String="",
    val mode : String="",
    val gender:String="",
    val salary : Int=0,
    val description : String="",
    val companyName : String="",
    val status : String = "Active",
    val location : String = "Penang,Malaysia",
    var numOfApp: Int = 0 )
