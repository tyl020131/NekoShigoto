package com.example.nekoshigoto

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    //private lateinit var dialog : AlertDialog.Builder
    private lateinit var pdfUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //to change title of activity
        val actionBar = supportActionBar
        actionBar!!.title = "Setup Profile"

        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.uploadImage.setOnClickListener{

//            dialog = AlertDialog.Builder(this)
//
//            dialog.setTitle("Select Image ")
//                .setMessage("Select Image From: ")
//                .setCancelable(true)
//                .setPositiveButton("Gallery"){dialogInterface,it->
////                    val intent = Intent()
////                    intent.action = Intent.ACTION_GET_CONTENT
////                    intent.type = "image/*"
////                    startActivityForResult(intent, 1)
//                    ImagePicker.with(this).galleryOnly().galleryMimeTypes(arrayOf("image/*"))
//                        .crop()	    			        //Crop image(Optional), Check Customization for more option
//                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//                        .start()
//                }
//                .setNegativeButton("Camera"){dialogInterface,it->
//                    ImagePicker.with(this).cameraOnly().crop().start()
//                }
//                .show()

            val choice = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            myAlertDialog.setTitle("Select Image")
            myAlertDialog.setCancelable(true)
            myAlertDialog.setItems(choice, DialogInterface.OnClickListener { dialog, item ->
                when {
                    // Select "Choose from Gallery" to pick image from gallery
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
                    // Select "Cancel" to cancel the task
                    choice[item] == "Cancel" -> {

                    }
                }
            })
            myAlertDialog.show()

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

//        //to create datepicker dialog
//        val calendar = Calendar.getInstance()
//        val datePicker = DatePickerDialog.OnDateSetListener{view, year, month, day ->
//            calendar.set(Calendar.YEAR, year)
//            calendar.set(Calendar.MONTH, month)
//            calendar.set(Calendar.DAY_OF_MONTH, day)
//            updateField(calendar)
//        }
//
//        binding.editTextDob.setOnClickListener{
//            DatePickerDialog(this, datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
//        }

        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            updateField(calendar)
        }

        // Set max date to today's date
        val maxDate = System.currentTimeMillis()
        val datePickerDialog = DatePickerDialog(
            this,
            datePicker,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = maxDate

        binding.editTextDob.setOnClickListener {
            datePickerDialog.show()
        }

        binding.button2.setOnClickListener{
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, 12)
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
        when(requestCode){
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