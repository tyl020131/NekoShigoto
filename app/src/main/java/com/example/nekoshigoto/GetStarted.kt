package com.example.nekoshigoto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class GetStarted : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        var button = findViewById<View>(R.id.button)
        var button2 = findViewById<View>(R.id.button2)
        button.setOnClickListener {
            val intent = Intent(this, EmployeeLogin::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener{
            val intent = Intent(this, CompanyLogin::class.java)
            startActivity(intent)
        }
    }

}