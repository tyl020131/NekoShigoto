package com.example.nekoshigoto

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class Logout : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)
        auth = FirebaseAuth.getInstance()
        // Get the instance of the shared preferences
        val sharedPreferences = getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)

        // Get the editor for the shared preferences
        val editor = sharedPreferences.edit()

        editor.clear()
        editor.commit()
        auth.signOut()

        startActivity(Intent(this, GetStarted::class.java))
    }
}