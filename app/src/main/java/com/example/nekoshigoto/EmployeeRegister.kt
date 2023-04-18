package com.example.nekoshigoto

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.nekoshigoto.databinding.ActivityEmployeeRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class EmployeeRegister : AppCompatActivity() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var binding:ActivityEmployeeRegisterBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val eye = binding.eyeView
        val passwordField = binding.passwordText
        val cpasswordField = binding.cpasswordText
        val emailField = binding.emailText

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.loginButton.setOnClickListener{
            val intent = Intent(this, EmployeeLogin::class.java)
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener {
            val email = emailField.text.toString().trim{it<=' '}
            val password = passwordField.text.toString().trim{it<=' '}
            val cpassword = cpasswordField.text.toString().trim{it<=' '}
            if(validation(email, password, cpassword)){
                auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = Customer(email, "jobSeeker", "S")
                        db.collection("User").document(email).set(user)
                        val intent = Intent(this, SetupProfile::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)

                        Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show()
                    } else {

                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()

                    }
                }.addOnFailureListener {
                    val message = "Error occurred ${it.localizedMessage}"
                    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    snackbar.setAction("Dismiss") { snackbar.dismiss() }
                    snackbar.show()
                }
            }

        }

        eye.setOnClickListener{
            if (passwordField.transformationMethod == PasswordTransformationMethod.getInstance()) {
                passwordField.transformationMethod = HideReturnsTransformationMethod.getInstance()
                cpasswordField.transformationMethod = HideReturnsTransformationMethod.getInstance()
                eye.setImageResource(R.drawable.eye_close)
            } else {
                passwordField.transformationMethod = PasswordTransformationMethod.getInstance()
                cpasswordField.transformationMethod = PasswordTransformationMethod.getInstance()
                eye.setImageResource(R.drawable.eye_open)
            }
        }
    }

    private fun validation(email: String, password: String, cpassword:String):Boolean
    {
        var error:String=""
        return if(email == "" || password == "" || cpassword == "") {
            Toast.makeText(this, "All input fields must be filled in", Toast.LENGTH_SHORT).show()
            false
        }else {
            error += isEmailValid(email)
            error += isPasswordValid(password, cpassword)
            if(error!="")
            {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage(error)
                builder.setPositiveButton("OK") { dialog, which ->

                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
                false
            }
            error.isEmpty()
        }

    }
    private fun isEmailValid(email: String): String {
        return if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            "Invalid email\n\n"
        } else
            ""
    }
    private fun isPasswordValid(password:String, cpassword: String):String{
        var message:String=""
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,15}$".toRegex()
        if(!passwordPattern.matches(password)){
            message+="Password must be at least 1 uppercase, 1 lowercase, 1 digit and the length must between 8 to 15"
        } else if(password != cpassword){
            message+="Password and confirm password do not match"
        }
        return message
    }



}