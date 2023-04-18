package com.example.nekoshigoto

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nekoshigoto.databinding.ActivitySetupProfileBinding
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog.Companion.setOnCountrySelected
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog.Companion.show
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class SetupProfile : AppCompatActivity() {
    private lateinit var binding : ActivitySetupProfileBinding
    private lateinit var selectedImg : Uri
    private var error : Boolean = false
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var storage : FirebaseStorage
    private lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        email = intent.getStringExtra("email").toString()

        //to change title of activity
        val actionBar = supportActionBar
        actionBar!!.title = "Setup Profile"

        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding.apply {
            errorTextFname.visibility = View.GONE
            errorTextLname.visibility = View.GONE
            errorTextGender.visibility = View.GONE
            errorTextNationality.visibility = View.GONE
            errorTextDOB.visibility = View.GONE
            errorTextContact.visibility = View.GONE
            errorTextIcno.visibility = View.GONE
            errorTextCountry.visibility = View.GONE
            errorTextState.visibility = View.GONE
            errorTextProfile.visibility = View.GONE
        }

        storage = FirebaseStorage.getInstance()

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
            binding.icno.text = "IC No"
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

        //to create datepicker dialog
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            updateField(calendar)
        }

        // Set max date to today's date
        val minus15 = Calendar.getInstance()
        minus15.add(Calendar.YEAR, -15)
        val maxDate = minus15.timeInMillis

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
            //perform validation -> if true then upload
            error = false

            val fnameT =  binding.editTextFName
            val lnameT = binding.editTextLName
            val genderGroupT = binding.genderGroup
            val nationalityGroupT = binding.nationalityGroup
            val dobT = binding.editTextDob
            val contactNoT = binding.editTextContact
            val icnoT = binding.editTextIcno
            val countryT = binding.editTextCountry
            val stateT = binding.editTextState

            if (!this::selectedImg.isInitialized)
            {
                error = true
                binding.errorTextProfile.visibility = View.VISIBLE
                binding.errorTextProfile.text = "Please upload an image"
            }

            checkfName(fnameT.text.toString())
            checklName(lnameT.text.toString())
            checkGender(genderGroupT)
            checkNationality(nationalityGroupT)
            checkDOB(dobT.text.toString())
            checkContact(contactNoT.text.toString())
            checkICNo(icnoT.text.toString())
            checkCountry(countryT.text.toString())
            checkState(stateT.text.toString())

            if(error)
            {
                Toast.makeText(this, "Please check the error message", Toast.LENGTH_SHORT).show()
            }
            else
            {
                uploadImage { imageUrl ->
                    val fname = fnameT.text.toString()
                    val lname = lnameT.text.toString()
                    val gender : RadioButton = genderGroupT.findViewById(genderGroupT.checkedRadioButtonId)
                    val nationality : RadioButton = nationalityGroupT.findViewById(nationalityGroupT.checkedRadioButtonId)
                    val dob = dobT.text.toString()
                    val contactNo = contactNoT.text.toString()
                    val icno = icnoT.text.toString()
                    val country = countryT.text.toString()
                    val state = stateT.text.toString()

                    val user = JobSeeker(fname, lname, email, gender.text.toString(), dob, nationality.text.toString(), contactNo, icno, imageUrl, country, state)
                    db.collection("Job Seeker").document(email).set(user)
                    val test = db.collection("User").document(email)
                    test.update("status", "A")
                    val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)
                    val myEdit: SharedPreferences.Editor = sharedPreferences.edit()
                    myEdit.putString("userid", email)
                    myEdit.putString("type", "jobseeker")
                    myEdit.putBoolean("loggedIn", true)
                    myEdit.commit()

                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)

                    Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun checkfName(fname:String){
        val regex = "^[a-zA-Z]+(?:\\s[a-zA-Z]+)*$".toRegex()
        if(fname.isEmpty()){
            binding.errorTextFname.visibility = View.VISIBLE
            binding.errorTextFname.text = "Please enter your first name"
            error = true
        }
        else if(!fname.matches(regex)) {
            binding.errorTextFname.visibility = View.VISIBLE
            binding.errorTextFname.text = "Invalid first name"
            error = true
        }
        else{
            binding.errorTextFname.visibility = View.GONE
        }
    }

    private fun checklName(lname:String){
        val regex = "^[a-zA-Z]+(?:\\s[a-zA-Z]+)*$".toRegex()
        if(lname.isEmpty()){
            binding.errorTextLname.visibility = View.VISIBLE
            binding.errorTextLname.text = "Please enter your last name"
            error = true
        }
        else if(!lname.matches(regex)) {
            binding.errorTextLname.visibility = View.VISIBLE
            binding.errorTextLname.text = "Invalid last name"
            error = true
        }
        else{
            binding.errorTextLname.visibility = View.GONE
        }
    }

    private fun checkGender(genderGroup:RadioGroup):Boolean{
        return if (genderGroup.checkedRadioButtonId == -1) {
            binding.errorTextGender.visibility = View.VISIBLE
            binding.errorTextGender.text = "Please select your gender"
            error = true
            true

        } else {
            binding.errorTextGender.visibility = View.GONE
            false
        }
    }

    private fun checkNationality(nationality:RadioGroup):Boolean{
        return if (nationality.checkedRadioButtonId == -1) {
            binding.errorTextNationality.visibility = View.VISIBLE
            binding.errorTextNationality.text = "Please select your nationality"
            error = true
            true

        } else {
            binding.errorTextNationality.visibility = View.GONE
            false
        }
    }

    private fun checkDOB(dob:String): Boolean{
        return if(dob == "Click here to select DOB"){
            binding.errorTextDOB.visibility = View.VISIBLE
            binding.errorTextDOB.text = "Please select your date of birth"
            error = true
            true
        }
        else if(calculateAge(dob)<15){
            binding.errorTextDOB.visibility = View.VISIBLE
            binding.errorTextDOB.text = "You must at least 15 years old"
            error = true
            true
        }
        else{
            binding.errorTextDOB.visibility = View.GONE
            false
        }
    }
    private fun calculateAge(dateString: String): Int {
        val birthDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(dateString)
        val today = Calendar.getInstance().time
        val diffInMillis = today.time - birthDate.time
        val age = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) / 365
        return age.toInt()
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

    private fun checkICNo(ICNo:String){
        if(binding.icno.text == "Passport No")
        {
            val regex = "^[A-Z0-9]{8,15}$".toRegex()
            if(ICNo.isEmpty()){
                binding.errorTextIcno.visibility = View.VISIBLE
                binding.errorTextIcno.text = "Please enter your passport No"
                error = true
            }
            else if(!ICNo.matches(regex)){
                binding.errorTextIcno.visibility = View.VISIBLE
                binding.errorTextIcno.text = "Invalid Passport No"
                error = true
            }
            else{
                binding.errorTextIcno.visibility = View.GONE
            }
        }
        else{
            val regex = "^\\d{6}-\\d{2}-\\d{4}\$".toRegex()
            if(ICNo.isEmpty()){
                binding.errorTextIcno.visibility = View.VISIBLE
                binding.errorTextIcno.text = "Please enter your IC No"
                error = true
            }
            else if(!ICNo.matches(regex)){
                binding.errorTextIcno.visibility = View.VISIBLE
                binding.errorTextIcno.text = "Invalid IC number"
                error = true
            }
            else{
                if(!checkGender(binding.genderGroup) && !checkDOB(binding.editTextDob.text.toString())){
                    val genderID = binding.genderGroup.checkedRadioButtonId
                    val gender : RadioButton = binding.genderGroup.findViewById(genderID)
                    val check = checkICFormatWithInput(ICNo, binding.editTextDob.text.toString(), gender.text.toString())
                    if(!check){
                        binding.errorTextIcno.visibility = View.VISIBLE
                        binding.errorTextIcno.text = "IC No is not match with your DOB or gender"
                        error = true
                    } else{
                        binding.errorTextIcno.visibility = View.GONE
                    }
                }else{
                    binding.errorTextIcno.visibility = View.VISIBLE
                    binding.errorTextIcno.text = "Please ensure that you have selected your DOB and gender"
                    error = true                }
            }
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
            return if(!checkNationality(binding.nationalityGroup))
            {
                val nationalityID = binding.nationalityGroup.checkedRadioButtonId
                val nationality : RadioButton = binding.nationalityGroup.findViewById(nationalityID)
                return if((nationality.text.toString() == "Malaysian" && country != "Malaysia") || (nationality.text.toString() != "Malaysian" && country == "Malaysia"))
                {
                    binding.errorTextCountry.visibility = View.VISIBLE
                    binding.errorTextCountry.text = "You have selected a different country with your nationality"
                    error = true
                    true
                }
                else
                {
                    binding.errorTextCountry.visibility = View.GONE
                    false
                }
            }
            else{
                binding.errorTextCountry.visibility = View.VISIBLE
                binding.errorTextCountry.text = "Please ensure that you have selected your nationality"
                error = true
                true
            }

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

    private fun checkICFormatWithInput(ICNo: String, date: String, gender: String):Boolean {
        Log.i("test", "test")
        // The IC number and date string
        val dateString = date

        // Extract the birthdate from the IC number
        val birthdate = ICNo.substring(0, 6)

        // Parse the date string into a Date object
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = dateFormatter.parse(dateString)

        // Extract the day, month, and year components from the date
        val calendar = Calendar.getInstance()
        calendar.time = date
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR) % 100

        // Extract the day, month, and year components from the birthdate
        val birthdateDay = birthdate.substring(4).toInt()
        val birthdateMonth = birthdate.substring(2, 4).toInt()
        val birthdateYear = birthdate.substring(0, 2).toInt()

        // Compare the components
        return if (day == birthdateDay && month == birthdateMonth && year == birthdateYear) {
            Log.i("GameFragment", "Called ViewModelProvider.get")
            // The date matches the birthdate in the IC number
            val lastChar = ICNo.substring(ICNo.length - 1)
            if(lastChar.toInt() % 2 == 0 && gender == "Female")
                true
            else lastChar.toInt() % 2 == 1 && gender == "Male"
        } else {
            // The date does not match the birthdate in the IC number
            Log.i("GameFragment", "sohai")
            false
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

    private fun updateField(calendar: Calendar) {
        val format = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)

        binding.editTextDob.text = (sdf.format(calendar.time))
    }

    private fun uploadImage(callback: (String) -> Unit) {
        val reference = storage.reference.child("Employee").child(Date().time.toString() + randomString(8))
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

        when(requestCode){
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