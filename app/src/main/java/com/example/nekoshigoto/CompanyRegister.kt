package com.example.nekoshigoto

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.nekoshigoto.databinding.ActivityCompanyRegisterBinding
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog.Companion.setOnCountrySelected
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog.Companion.show
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class CompanyRegister : AppCompatActivity() {

    private lateinit var binding : ActivityCompanyRegisterBinding
    private lateinit var selectedImg : Uri
    private var error : Boolean = false
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage : FirebaseStorage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Register an Account"
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.apply {
            errorTextName.visibility = View.GONE
            errorTextEmail.visibility = View.GONE
            errorTextContact.visibility = View.GONE
            errorTextAddress.visibility = View.GONE
            errorTextCountry.visibility = View.GONE
            errorTextState.visibility = View.GONE
            errorTextBusiness.visibility = View.GONE
            errorTextProfile.visibility = View.GONE
        }

        binding.editTextCountry.setOnClickListener{
            CountrySelectionDialog().create(this)
                .show()?.setOnCountrySelected {
                    binding.editTextCountry.text = it.name
                }
        }

        binding.uploadImage.setOnClickListener{

            val choice = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            myAlertDialog.setTitle("Select Image")
            myAlertDialog.setCancelable(true)
            myAlertDialog.setItems(choice, DialogInterface.OnClickListener { dialog, item ->
                when {

                    choice[item] == "Choose from Gallery" -> {
                        ImagePicker.with(this).galleryOnly().galleryMimeTypes(arrayOf("image/*"))
                            .crop()	    			        //Crop image(Optional), Check Customization for more option
                            .compress(1024)			//Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start()
                    }
                    // Select "Take Photo" to take a photo
                    choice[item] == "Take Photo" -> {
                        ImagePicker.with(this).cameraOnly().crop().start()
                    }
                }
            })
            myAlertDialog.show()

        }

        binding.registerButton.setOnClickListener{
            error = false

            val nameT =  binding.editTextName
            val emailT = binding.editTextEmail
            val contactNoT = binding.editTextContact
            val addressT = binding.editTextAddress
            val countryT = binding.editTextCountry
            val stateT = binding.editTextState
            val businessT = binding.editTextBusiness

            if (!this::selectedImg.isInitialized)
            {
                error = true
                binding.errorTextProfile.visibility = View.VISIBLE
                binding.errorTextProfile.text = "Please upload an image"
            }
            checkName(nameT.text.toString())
            checkEmail(emailT.text.toString())
            checkAddress(addressT.text.toString())
            checkBusiness(businessT.text.toString())
            checkContact(contactNoT.text.toString())
            checkCountry(countryT.text.toString())
            checkState(stateT.text.toString())

            if(error)
            {
                Toast.makeText(this, "Please check the error message", Toast.LENGTH_SHORT).show()
            }
            else
            {
                uploadImage { imageUrl ->
                    val name = nameT.text.toString()
                    val email = emailT.text.toString()
                    val contactNo = contactNoT.text.toString()
                    val country = countryT.text.toString()
                    val state = stateT.text.toString()
                    val address = addressT.text.toString()
                    val business = businessT.text.toString()

                    val user = Company(name, email, contactNo, address, country, state, imageUrl, business, "P")
                    db.collection("Company").document(email).set(user)

                    //this is used to change the company account to approved status

                    //val test = db.collection("User").document(email)
                    //test.update("status", "A")

                    val intent = Intent(this, GetStarted::class.java)
                    startActivity(intent)

                    Toast.makeText(this, "Successfully registered, please wait for the approval email", Toast.LENGTH_SHORT).show()

                }
            }
        }

    }

    private fun checkName(fname:String){
        if(fname.isEmpty()){
            binding.errorTextName.visibility = View.VISIBLE
            binding.errorTextName.text = "Please enter your first name"
            error = true
        }
        else if(fname.length<5) {
            binding.errorTextName.visibility = View.VISIBLE
            binding.errorTextName.text = "Name must be at least 5 characters"
            error = true
        }
        else{
            binding.errorTextName.visibility = View.GONE
        }
    }

    private fun checkEmail(email: String) {

        if(email.isEmpty()){
            binding.errorTextEmail.visibility = View.VISIBLE
            binding.errorTextEmail.text = "Please enter your email"
            error = true
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.errorTextEmail.visibility = View.VISIBLE
            binding.errorTextEmail.text = "Invalid email"
            error = true
        }
        else{
            db.collection("Company").document(email).get().addOnSuccessListener { documents ->
                if (documents.exists()) {
                    binding.errorTextEmail.visibility = View.VISIBLE
                    binding.errorTextEmail.text = "This email has already been registered for an account"
                    error = true
                } else {
                    binding.errorTextEmail.visibility = View.GONE
                }
            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }

        }
    }

    private fun checkContact(contactNo:String){
        val regex = "^01\\d-\\d{7,8}$".toRegex()
        if(contactNo.isEmpty())
        {
            binding.errorTextContact.visibility = View.VISIBLE
            binding.errorTextContact.text = "Please enter your contact number"
            error = true
        }
        else if(!contactNo.matches(regex))
        {
            binding.errorTextContact.visibility = View.VISIBLE
            binding.errorTextContact.text = "Invalid Contact Number (xxx-xxxxxxxx)"
            error = true
        }
        else{
            binding.errorTextContact.visibility = View.GONE
        }
    }

    private fun checkCountry(country:String): Boolean {
        return if(country=="Click here to select country"){
            binding.errorTextCountry.visibility = View.VISIBLE
            binding.errorTextCountry.text = "Please select your country"
            error = true
            true
        }
        else{
            binding.errorTextCountry.visibility = View.GONE
            false
        }
    }

    private fun checkBusiness(business:String){
        if(business.isEmpty()){
            binding.errorTextBusiness.visibility = View.VISIBLE
            binding.errorTextBusiness.text = "Please enter your company's business"
            error = true
        }
        else{
            binding.errorTextBusiness.visibility = View.GONE
        }
    }

    private fun checkAddress(address:String){
        if(address.isEmpty()){
            binding.errorTextAddress.visibility = View.VISIBLE
            binding.errorTextAddress.text = "Please enter your company's business"
            error = true
        }
        else{
            binding.errorTextAddress.visibility = View.GONE
        }
    }

    private fun checkState(state:String){
        val regex = "^[a-zA-Z]+(?:\\s[a-zA-Z]+)*$".toRegex()
        if(state.isEmpty()){
            binding.errorTextState.visibility = View.VISIBLE
            binding.errorTextState.text = "Please enter your State"
            error = true
        }
        else if(!state.matches(regex)) {
            binding.errorTextState.visibility = View.VISIBLE
            binding.errorTextState.text = "Invalid State"
            error = true
        }
        else{
            binding.errorTextState.visibility = View.GONE
        }
    }

    private fun uploadImage(callback: (String) -> Unit) {
        val reference = storage.reference.child("Company").child(Date().time.toString() + randomString(8))
        reference.putFile(selectedImg).addOnCompleteListener{
            if(it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    callback(imageUrl)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            2404 -> if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                selectedImg = data?.data!!
                binding.errorTextProfile.visibility = View.GONE
                binding.uploadImage.setImageURI(selectedImg)

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun randomString(n: Int): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..n)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

}