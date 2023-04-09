package com.example.nekoshigoto

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ancient.country.view.fragment.CountryListDialogFragment
import com.example.nekoshigoto.databinding.ActivitySetupProfileBinding
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog.Companion.setOnCountrySelected
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog.Companion.show
import com.github.dhaval2404.imagepicker.ImagePicker
import java.text.SimpleDateFormat
import java.util.*


class SetupProfile : AppCompatActivity() {
    private lateinit var binding : ActivitySetupProfileBinding
    private lateinit var selectedImg : Uri
    private lateinit var dialog : AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //to change title of activity
        val actionBar = supportActionBar
        actionBar!!.title = "Setup Profile"



        binding.uploadImage.setOnClickListener{

            dialog = AlertDialog.Builder(this)

            dialog.setTitle("Select Image ")
                .setMessage("Select Image From: ")
                .setCancelable(true)
                .setPositiveButton("Gallery"){dialogInterface,it->
//                    val intent = Intent()
//                    intent.action = Intent.ACTION_GET_CONTENT
//                    intent.type = "image/*"
//                    startActivityForResult(intent, 1)
                    ImagePicker.with(this).galleryOnly().galleryMimeTypes(arrayOf("image/*"))
                        .crop()	    			        //Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start()
                }
                .setNegativeButton("Camera"){dialogInterface,it->
                    ImagePicker.with(this).cameraOnly().crop().start()
                }
                .show()

        }

        binding.radioButton.setOnClickListener {
            binding.icno.text = "ICNO"
        }
        binding.radioButton2.setOnClickListener {
            binding.icno.text = "Passport No"
        }

        binding.editTextCountry.setOnClickListener{
            CountrySelectionDialog().create(this)
                .show()?.setOnCountrySelected {
                    binding.editTextCountry.text = it.name
                }
        }

        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener{view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            updateField(calendar)

        }

        binding.editTextDob.setOnClickListener{
            DatePickerDialog(this, datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.button2.setOnClickListener{
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }

    private fun updateField(calendar: Calendar) {
        val format = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)

        binding.editTextDob.text = (sdf.format(calendar.time))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if(data != null) {
//            selectedImg = data.data!!
//
//            binding.uploadImage.setImageURI(selectedImg)
//        }
        if(requestCode == 2404)
        {
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!
                Toast.makeText(this, requestCode.toString(), Toast.LENGTH_SHORT).show()

                // Use Uri object instead of File to avoid storage permissions
                binding.uploadImage.setImageURI(uri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }


    }
}