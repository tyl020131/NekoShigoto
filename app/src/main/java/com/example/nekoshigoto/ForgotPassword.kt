package com.example.nekoshigoto

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.nekoshigoto.databinding.ActivityForgotPasswordBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ForgotPassword : AppCompatActivity() {

    private lateinit var binding : ActivityForgotPasswordBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var snackbar : Snackbar
    private lateinit var progressDialog : ProgressDialog
    private lateinit var sh : SharedPreferences
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sh = this.getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)

        auth = FirebaseAuth.getInstance()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)

        binding.button2.setOnClickListener {
            changePassword()
        }
        setContentView(binding.root)
    }

    private fun changePassword(){
        var view = binding.root
        val oldP = binding.editTextOldPassword.text.toString()
        val newP= binding.editTextPassword.text.toString()
        val cPass = binding.editTextCPassword.text.toString()
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,15}$".toRegex()

        if(oldP.isNotEmpty() && newP.isNotEmpty() && cPass.isNotEmpty()) {
            if(passwordPattern.matches(newP)){
                progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Updating...")
                progressDialog.show()
                if(cPass==newP){
                    val user = auth.currentUser
                    if(user != null){
                        val credential = EmailAuthProvider
                            .getCredential(user.email!!, oldP)

                        user.reauthenticate(credential)
                            .addOnCompleteListener {
                                if(it.isSuccessful){
                                    progressDialog.dismiss()
                                    user!!.updatePassword(newP)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                auth.signOut()
                                                Toast.makeText(this, "Password updated successfully.", Toast.LENGTH_LONG).show()
                                                if(sh.getString("type", "").toString() == "jobseeker"){
                                                    val myEdit: SharedPreferences.Editor = sh.edit()
                                                    var email = intent.getStringExtra("email").toString()
                                                    db.collection("User").document(email).update("status", "A")
                                                    myEdit.putBoolean("loggedIn", true)
                                                    myEdit.commit()
                                                    startActivity(Intent(this, Home::class.java))
                                                    finish()
                                                }
                                                else if(sh.getString("type", "").toString() == "company"){

                                                    val myEdit: SharedPreferences.Editor = sh.edit()
                                                    var email = intent.getStringExtra("email").toString()
                                                    db.collection("User").document(email).update("status", "A")
                                                    myEdit.putBoolean("loggedIn", true)
                                                    myEdit.commit()
                                                    startActivity(Intent(this, CompanyHome::class.java))
                                                    finish()
                                                }
                                                else{
                                                    val intent = Intent(this, GetStarted::class.java)
                                                    startActivity(intent)
                                                }
                                            }
                                        }
                                } else{
                                    progressDialog.dismiss()
                                    var message = "You have entered a wrong current password, please try again"
                                    snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                                    snackbar.setAction("Dismiss") { snackbar.dismiss() }
                                    snackbar.show()
                                }
                            }
                    }else{
                        progressDialog.dismiss()
                        val intent = Intent(this, GetStarted::class.java)
                        startActivity(intent)
                    }
                }
                else{
                    progressDialog.dismiss()
                    var message = "New password and confirm password do not match"
                    snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                    snackbar.setAction("Dismiss") { snackbar.dismiss() }
                    snackbar.show()
                }
            }
            else {
                var message = "Password Format: 8-15 characters with 1 uppercase, 1 lowercase, 1 digit"
                snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") { snackbar.dismiss() }
                snackbar.show()
            }
        }
        else{

            var message = "Please enter all the fields"
            snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar.setAction("Dismiss") { snackbar.dismiss() }
            snackbar.show()
        }
    }
}