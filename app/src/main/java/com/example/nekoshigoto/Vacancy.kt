package com.example.nekoshigoto

data class Vacancy(val image:String="",val position : String="", val field : String="", val mode : String="", val gender:String="", val salary : Double=0.00, val description : String="",val companyName : String="", val status : String = "Active", var numOfApp: Int = 0 )
