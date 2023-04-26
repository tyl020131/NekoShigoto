package com.example.nekoshigoto

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.nekoshigoto.databinding.ActivityCompanyLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CompanyLogin : AppCompatActivity() {

    private lateinit var binding : ActivityCompanyLoginBinding
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var auth : FirebaseAuth
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        val registerButton = binding.registerButton
        val eye = binding.eyeView
        val passwordField = binding.passwordText
        val emailField = binding.emailText

        // Initialize Firebase Auth
        auth = Firebase.auth

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
                                        "company" -> {
                                            when (user.status) {
                                                "A" -> {
                                                    Toast.makeText(baseContext, "Login Successfully",
                                                        Toast.LENGTH_SHORT).show()
                                                    val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)
                                                    val myEdit: SharedPreferences.Editor = sharedPreferences.edit()
                                                    myEdit.putString("userid", email)
                                                    myEdit.putString("type", "company")
                                                    myEdit.putBoolean("loggedIn", true)
                                                    myEdit.commit()

                                                    val intent = Intent(this, CompanyHome::class.java)
                                                    startActivity(intent)

                                                }
                                                "C" -> {
                                                    Toast.makeText(baseContext, "Please change your password",
                                                        Toast.LENGTH_SHORT).show()
                                                    val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)
                                                    val myEdit: SharedPreferences.Editor = sharedPreferences.edit()
                                                    myEdit.putString("userid", email)
                                                    myEdit.putString("type", "company")

                                                    myEdit.commit()
                                                    val intent = Intent(this, ForgotPassword::class.java)
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
                                            val message = "This email address is not registered as a company account."
                                            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                                            snackbar.setAction("Dismiss") { snackbar.dismiss() }
                                            snackbar.show()
                                        }
                                    }
                                }
                                .addOnFailureListener{
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
        }

        binding.forgotPassword.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Forgot Password")
            val view = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
            val username = view.findViewById<EditText>(R.id.et_email)
            builder.setView(view)
            builder.setPositiveButton("Reset"){dialogInterface,it->
                forgotPassword(username.text.toString())

            }.setNegativeButton("Cancel"){dialogInterface,it->

            }
            builder.show()
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

    private fun forgotPassword(email: String)
    {
        if(email == "") {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            return
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
            return
        }
        else{
            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Sending email...")
            progressDialog.show()
            auth.sendPasswordResetEmail(email).addOnSuccessListener {
                db.collection("User").document(email).update("status", "C")
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Successfully sent a reset email", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Toast.makeText(this, "Error updating document", Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener{
                progressDialog.dismiss()
                Toast.makeText(this, "This email hasn't registered an account", Toast.LENGTH_SHORT).show()
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