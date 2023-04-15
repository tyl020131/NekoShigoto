package com.example.nekoshigoto

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nekoshigoto.databinding.ActivityEmployeeRegisterBinding

class EmployeeRegister : AppCompatActivity() {

    private lateinit var binding:ActivityEmployeeRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loginButton.setOnClickListener{view->

            val intent = Intent(this, EmployeeLogin::class.java)
            startActivity(intent)
        }
    }
}