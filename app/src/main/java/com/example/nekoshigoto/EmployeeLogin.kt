package com.example.nekoshigoto

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.nekoshigoto.databinding.ActivityEmployeeLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class EmployeeLogin : AppCompatActivity() {
    private lateinit var binding: ActivityEmployeeLoginBinding
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var auth : FirebaseAuth
    private lateinit var progressDialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val eye = binding.eyeView
        val passwordField = binding.passwordText
        val emailField = binding.emailText

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, EmployeeRegister::class.java)
            startActivity(intent)
        }
        binding.loginButton.setOnClickListener {
            val email = emailField.text.toString().trim{it<=' '}
            val password = passwordField.text.toString().trim{it<=' '}
            if(validation(email, password)){
                progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Signing in...")
                progressDialog.show()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            progressDialog.dismiss()
                            db.collection("User").document(email).get()
                                .addOnSuccessListener {
                                    val user = it.toObject<Customer>()  //convert the doc into object
                                    when(user?.userType){
                                        "jobSeeker" -> {
                                            when (user.status) {
                                                "A" -> {
                                                    val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)
                                                    val myEdit: SharedPreferences.Editor = sharedPreferences.edit()
                                                    myEdit.putString("userid", email)
                                                    myEdit.putString("type", "jobseeker")
                                                    myEdit.putBoolean("loggedIn", true)
                                                    myEdit.commit()

                                                    val intent = Intent(this, Home::class.java)
                                                    startActivity(intent)
                                                }
                                                "S" -> {
                                                    val intent = Intent(this, SetupProfile::class.java)
                                                    intent.putExtra("email", email)
                                                    startActivity(intent)
                                                }
                                                else -> {
                                                    val message = "This account has been banned"
                                                    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                                                    snackbar.setAction("Dismiss") { snackbar.dismiss() }
                                                    snackbar.show()
                                                }
                                            }
                                        }
                                        else -> {
                                            val message = "This email address is not registered as a job seeker account."
                                            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                                            snackbar.setAction("Dismiss") { snackbar.dismiss() }
                                            snackbar.show()
                                        }
                                    }
                                }
                                .addOnFailureListener{
                                    progressDialog.dismiss()
                                    val message = "Error occurred ${it.localizedMessage}"
                                    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                                    snackbar.setAction("Dismiss") { snackbar.dismiss() }
                                    snackbar.show()
                                }

                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(baseContext, "Email and password do not match",
                                Toast.LENGTH_SHORT).show()

                        }
                    }

            }
//            val intent = Intent(this, SetupProfile::class.java)
//            startActivity(intent)
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

    private fun validation(email: String, password: String):Boolean
    {
        return if(email == "" || password == "") {
            Toast.makeText(this, "All input fields must be filled in", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
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