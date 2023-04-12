package com.example.nekoshigoto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.nekoshigoto.databinding.ActivityCompanyLoginBinding

class CompanyLogin : AppCompatActivity() {

    private lateinit var binding : ActivityCompanyLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val loginButton = binding.loginButton
        val registerButton = binding.registerButton
        loginButton.setOnClickListener{
            val intent = Intent(this, CompanyHome::class.java)
            startActivity(intent)
        }
        registerButton.setOnClickListener{
            val intent = Intent(this, CompanyRegister::class.java)
            startActivity(intent)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}