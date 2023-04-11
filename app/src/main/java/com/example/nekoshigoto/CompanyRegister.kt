package com.example.nekoshigoto

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nekoshigoto.databinding.ActivityCompanyRegisterBinding
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog.Companion.setOnCountrySelected
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog.Companion.show
import com.github.dhaval2404.imagepicker.ImagePicker

class CompanyRegister : AppCompatActivity() {

    private lateinit var binding : ActivityCompanyRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Register an Account"

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


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if(data != null) {
//            selectedImg = data.data!!
//
//            binding.uploadImage.setImageURI(selectedImg)
//        }
        when (requestCode) {
            2404 -> if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!
                binding.uploadImage.setImageURI(uri)

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}