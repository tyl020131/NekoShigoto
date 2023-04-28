package com.example.nekoshigoto

data class AppDetails(
    var coverLetter : String,
    var jobSeeker : String,
    val resumeUrl : String,
    val status : String

)
{
    constructor() : this("", "", "", "")
}
