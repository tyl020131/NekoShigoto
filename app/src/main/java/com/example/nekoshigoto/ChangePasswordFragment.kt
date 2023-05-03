package com.example.nekoshigoto

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.nekoshigoto.databinding.FragmentChangePasswordBinding
import com.example.nekoshigoto.databinding.FragmentCompanyProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChangePasswordFragment : Fragment() {

    private lateinit var binding : FragmentChangePasswordBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var snackbar : Snackbar
    private lateinit var progressDialog : ProgressDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {

        auth = FirebaseAuth.getInstance()
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.button2.setOnClickListener {
            changePassword()
        }

        return view
    }

    private fun changePassword(){
        val oldP = binding.editTextOldPassword.text.toString()
        val newP= binding.editTextPassword.text.toString()
        val cPass = binding.editTextCPassword.text.toString()
        val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,15}$".toRegex()

        if(oldP.isNotEmpty() && newP.isNotEmpty() && cPass.isNotEmpty()) {
            if(passwordPattern.matches(newP)){
                progressDialog = ProgressDialog(requireContext())
                progressDialog.setMessage("Signing in...")
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
                                                Toast.makeText(requireContext(), "Password updated successfully. Please login again", Toast.LENGTH_LONG).show()
                                                val intent = Intent(requireContext(), GetStarted::class.java)
                                                startActivity(intent)
                                            }
                                        }
                                } else{
                                    progressDialog.dismiss()
                                    var message = "You have entered a wrong current password, please try again"
                                    snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
                                    snackbar.setAction("Dismiss") { snackbar.dismiss() }
                                    snackbar.show()
                                }
                            }
                    }else{
                        progressDialog.dismiss()
                        val intent = Intent(requireContext(), GetStarted::class.java)
                        startActivity(intent)
                    }
                }
                else{
                    var message = "New password and confirm password do not match"
                    snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
                    snackbar.setAction("Dismiss") { snackbar.dismiss() }
                    snackbar.show()
                }
            }
            else {
                var message = "Password Format: 8-15 characters with 1 uppercase, 1 lowercase, 1 digit"
                snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
                snackbar.setAction("Dismiss") { snackbar.dismiss() }
                snackbar.show()
            }
        }
        else{

            var message = "Please enter all the fields"
            snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
            snackbar.setAction("Dismiss") { snackbar.dismiss() }
            snackbar.show()
        }
    }
    
}