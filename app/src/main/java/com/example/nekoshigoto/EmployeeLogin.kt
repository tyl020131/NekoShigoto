package com.example.nekoshigoto

import android.content.Intent
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
//        getSupportFragmentManager()
//            .beginTransaction()
//            .replace(R.id.fragment_container, EmployeeLogin()).commit()
    }
}