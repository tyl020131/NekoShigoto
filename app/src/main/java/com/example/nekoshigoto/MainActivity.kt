package com.example.nekoshigoto

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("loggedIn", false)) {
            // Redirect to home screen
            if(sharedPreferences.getString("type", "").toString() == "jobseeker"){
                startActivity(Intent(this, Home::class.java))
                finish()
            }
            else if(sharedPreferences.getString("type", "").toString() == "company"){
                startActivity(Intent(this, CompanyHome::class.java))
                finish()
            }
            else {
                // Redirect to getStarted screen
                startActivity(Intent(this, GetStarted::class.java))
                finish()
            }

        } else {
            // Redirect to getStarted screen
            startActivity(Intent(this, GetStarted::class.java))
            finish()
        }
    }
}