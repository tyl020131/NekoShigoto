package com.example.nekoshigoto

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nekoshigoto.databinding.ActivityEmployeeLoginBinding

class EmployeeLogin : AppCompatActivity() {
    private lateinit var binding: ActivityEmployeeLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, EmployeeRegister::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener {
            val userid = binding.emailText.text.toString()
            val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)

            val myEdit: SharedPreferences.Editor = sharedPreferences.edit()

            myEdit.putString("userid", userid)
            myEdit.commit()
            val intent = Intent(this, SetupProfile::class.java)
            startActivity(intent)
        }

    }
}