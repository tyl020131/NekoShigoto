package com.example.nekoshigoto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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
        val eye = binding.eyeView
        val passwordField = binding.passwordText
        val emailField = binding.emailText

        loginButton.setOnClickListener{
            val intent = Intent(this, CompanyHome::class.java)
            startActivity(intent)
        }
        registerButton.setOnClickListener{
            val intent = Intent(this, CompanyRegister::class.java)
            startActivity(intent)
        }
        eye.setOnClickListener{
            if (passwordField.transformationMethod == PasswordTransformationMethod.getInstance()) {
                passwordField.transformationMethod = HideReturnsTransformationMethod.getInstance()
                eye.setImageResource(R.drawable.eye_close)
            } else {
                passwordField.transformationMethod = PasswordTransformationMethod.getInstance()
                eye.setImageResource(R.drawable.eye_open)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}