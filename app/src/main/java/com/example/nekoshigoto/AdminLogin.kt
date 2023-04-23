package com.example.nekoshigoto

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MenuItem
import android.widget.Toast
import com.example.nekoshigoto.databinding.ActivityAdminLoginBinding
import com.example.nekoshigoto.databinding.ActivityCompanyLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class AdminLogin : AppCompatActivity() {
    private lateinit var binding : ActivityAdminLoginBinding
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var auth : FirebaseAuth
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        val view = binding.root
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        // Initialize Firebase Auth
        auth = Firebase.auth

        val eye = binding.eyeView
        val passwordField = binding.passwordText
        val usernameField = binding.usernameText

        eye.setOnClickListener{
            if (passwordField.transformationMethod == PasswordTransformationMethod.getInstance()) {
                passwordField.transformationMethod = HideReturnsTransformationMethod.getInstance()
                eye.setImageResource(R.drawable.eye_close)
            } else {
                passwordField.transformationMethod = PasswordTransformationMethod.getInstance()
                eye.setImageResource(R.drawable.eye_open)
            }
        }

        binding.imageView4.setOnClickListener {
            val intent = Intent(this, Logout::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val userName = usernameField.text.toString().trim{it<=' '}
            val password = passwordField.text.toString().trim{it<=' '}
            if(validation(userName, password)){
                progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Signing in...")
                progressDialog.show()

                db.collection("Admin").document(userName).get()
                    .addOnSuccessListener{
                        if(it!=null){
                            val email = it.getString("email")
                            if(email != null){
                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {
                                            progressDialog.dismiss()
                                            Toast.makeText(baseContext, "Login Successfully",
                                                Toast.LENGTH_SHORT).show()
                                            val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)
                                            val myEdit: SharedPreferences.Editor = sharedPreferences.edit()
                                            myEdit.putString("userid", email)
                                            myEdit.putString("type", "admin")
                                            myEdit.putBoolean("loggedIn", true)
                                            myEdit.commit()

//                                            val intent = Intent(this, CompanyHome::class.java)
//                                            startActivity(intent)
                                        }else{
                                            progressDialog.dismiss()
                                            Toast.makeText(baseContext, "Username and password do not match",
                                                Toast.LENGTH_SHORT).show()
                                        }

                                    }
                            }else{
                                progressDialog.dismiss()
                                Toast.makeText(baseContext, "Username and password do not match",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }.addOnFailureListener{
                        val message = "Error occurred ${it.localizedMessage}"
                        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                        snackbar.setAction("Dismiss") { snackbar.dismiss() }
                        snackbar.show()
                    }

            }
        }

        setContentView(binding.root)
    }

    private fun validation(userName: String, password: String):Boolean
    {
        return if(userName == "" || password == "") {
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