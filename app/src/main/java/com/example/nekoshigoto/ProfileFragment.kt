package com.example.nekoshigoto

import FilterJobSeekerDialog
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.NumberFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.nekoshigoto.databinding.FragmentProfileBinding
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog.Companion.setOnCountrySelected
import com.fasilthottathil.countryselectiondialog.CountrySelectionDialog.Companion.show
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var selectedImg : Uri
    private var chgImg : Boolean = false
    private var error : Boolean = false
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var storage : FirebaseStorage
    private lateinit var email : String
    private lateinit var dialog : AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Disable the up button
        setHasOptionsMenu(true)

        storage = FirebaseStorage.getInstance()
        
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.apply {
            saveButton.visibility = View.GONE
            editLayout.visibility = View.GONE
            cancelButton.visibility = View.GONE
        }

//        email = sh.getString("userid","").toString()
//        db.collection("Job Seeker").document(email).get()
//            .addOnSuccessListener {
//                val user = it.toObject<JobSeeker>()  //convert the doc into object
//                binding.apply {
//                    //insert data for view layout
//                    textName.text = user?.fname + " " + user?.lname
//                    textGender.text = user?.gender
//                    textNationality.text = user?.nationality
//                    Glide.with(requireContext())
//                    .load(user?.profilePic)
//                    .into(binding.profileImage)
//                    textDOB.text = user?.dob
//                    textContact.text = user?.contactNo
//                    textIcNo.text = user?.icPassport
//                    textCountry.text = user?.country
//                    textState.text = user?.state
//                    val number = user?.salary
//                    val formattedNumber = NumberFormat.getNumberInstance(Locale.US).format(number)
//                    textSalary.text = "RM $formattedNumber"
//
//                    //insert data for edit layout
//                    editTextFName.setText(user?.fname)
//                    editTextLName.setText(user?.lname)
//                    editTextDob.text = user?.dob
//                    editTextContact.setText(user?.contactNo)
//                    editTextIcno.setText(user?.icPassport)
//                    editTextCountry.text = user?.country
//                    editTextState.setText(user?.state)
//                    editTextSalary.setText(user?.salary.toString())
//                    if(user?.gender == "Male")
//                        male.isChecked = true
//                    else
//                        female.isChecked = true
//
//                    if(user?.nationality == "Malaysian")
//                        radioButton.isChecked = true
//                    else
//                        radioButton2.isChecked = true
//
//                    Glide.with(requireContext())
//                        .load(user?.profilePic)
//                        .into(binding.uploadImage)
//
//                    binding.uploadImage.tag = user?.profilePic
//
//                }
//
//
//            }
//            .addOnFailureListener{
//                Log.e("SearchUser", it.message.toString())
//            }

        val viewModel = ViewModelProvider(requireActivity()).get(JobSeekerViewModel::class.java)
        var user : JobSeeker = viewModel.getJobSeeker()
        email = user.email

        binding.apply {
            //insert data for view layout
            textName.text = user?.fname + " " + user?.lname
            textGender.text = user?.gender
            textNationality.text = user?.nationality
            Glide.with(requireContext())
                .load(user?.profilePic)
                .into(binding.profileImage)
            textDOB.text = user?.dob
            textContact.text = user?.contactNo
            textIcNo.text = user?.icPassport
            textCountry.text = user?.country
            textState.text = user?.state
            val number = user?.salary
            val formattedNumber = NumberFormat.getNumberInstance(Locale.US).format(number)
            textSalary.text = "RM $formattedNumber"
            var workingMode = user?.workingMode?.split(", ")
            workingMode?.forEach {
                if(it == binding.freelance.text.toString())
                    binding.freelance.visibility = View.VISIBLE
                else if(it == binding.partTime.text.toString())
                    binding.partTime.visibility = View.VISIBLE
                else if(it == binding.fullTime.text.toString())
                    binding.fullTime.visibility = View.VISIBLE
            }

            //insert data for edit layout
            editTextFName.setText(user?.fname)
            editTextLName.setText(user?.lname)
            editTextDob.text = user?.dob
            editTextContact.setText(user?.contactNo)
            editTextIcno.setText(user?.icPassport)
            editTextCountry.text = user?.country
            editTextState.setText(user?.state)
            editTextSalary.setText(user?.salary.toString())
            val list = user?.workingMode?.split(", ")
            if(user?.gender == "Male")
                male.isChecked = true
            else
                female.isChecked = true

            if(user?.nationality == "Malaysian")
                radioButton.isChecked = true
            else
                radioButton2.isChecked = true

            Glide.with(requireContext())
                .load(user?.profilePic)
                .into(binding.uploadImage)

            binding.uploadImage.tag = user?.profilePic

            list?.forEach {
                if(it == binding.myCheckBox1.text.toString())
                    binding.myCheckBox1.isChecked = true
                else if(it == binding.myCheckBox2.text.toString())
                    binding.myCheckBox2.isChecked = true
                else if(it == binding.myCheckBox3.text.toString())
                binding.myCheckBox3.isChecked = true
            }
        }

        binding.editButton.setOnClickListener {
            binding.apply {
                scrollView2.smoothScrollTo(0, 0)
                saveButton.visibility = View.VISIBLE
                editLayout.visibility = View.VISIBLE
                cancelButton.visibility = View.VISIBLE

                viewLayout.visibility = View.GONE
                editButton.visibility = View.GONE
                qualificationButton.visibility = View.GONE

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
                errorTextSalary.visibility = View.GONE
                errorTextMode.visibility = View.GONE
            }
        }

        binding.cancelButton.setOnClickListener {
            binding.apply {
                scrollView2.smoothScrollTo(0, 0)
                saveButton.visibility = View.GONE
                editLayout.visibility = View.GONE
                cancelButton.visibility = View.GONE

                viewLayout.visibility = View.VISIBLE
                editButton.visibility = View.VISIBLE
                qualificationButton.visibility = View.VISIBLE
            }
        }

        binding.qualificationButton.setOnClickListener {

            it.findNavController().navigate(R.id.action_profileFragment_to_qualificationFragment)
            //it.findNavController().navigate(R.id.action_profileFragment_to_userDetailFragment)

        }

        binding.uploadImage.setOnClickListener{

            val choice = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
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
            CountrySelectionDialog().create(requireContext())
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
            requireContext(),
            datePicker,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = maxDate

        binding.editTextDob.setOnClickListener {
            datePickerDialog.show()
        }

        binding.saveButton.setOnClickListener {
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
            val salaryT = binding.editTextSalary

            checkfName(fnameT.text.toString())
            checklName(lnameT.text.toString())
            checkGender(genderGroupT)
            checkNationality(nationalityGroupT)
            checkDOB(dobT.text.toString())
            checkContact(contactNoT.text.toString())
            checkICNo(icnoT.text.toString())
            checkCountry(countryT.text.toString())
            checkState(stateT.text.toString())
            checkSalary(salaryT.text.toString())
            checkMode()
            if(error)
            {
                Toast.makeText(requireContext(), "Please check the error message", Toast.LENGTH_SHORT).show()
            }
            else
            {
                dialog = AlertDialog.Builder(requireContext())

                dialog.setTitle("Edit Confirmation ")
                    .setMessage("Are you sure to edit profile? ")
                    .setCancelable(true)
                    .setPositiveButton("Edit"){dialogInterface,it->
                        val loadedUrl = binding.uploadImage.tag as String
                        val fname = fnameT.text.toString()
                        val lname = lnameT.text.toString()
                        val gender : RadioButton = genderGroupT.findViewById(genderGroupT.checkedRadioButtonId)
                        val nationality : RadioButton = nationalityGroupT.findViewById(nationalityGroupT.checkedRadioButtonId)
                        val dob = dobT.text.toString()
                        val contactNo = contactNoT.text.toString()
                        val icno = icnoT.text.toString()
                        val country = countryT.text.toString()
                        val state = stateT.text.toString()
                        val salary = salaryT.text.toString().toInt()
                        var workingMode = ""
                        val partTime = binding.myCheckBox1
                        val fullTime = binding.myCheckBox2
                        val freelance = binding.myCheckBox3
                        if(partTime.isChecked){
                            workingMode += partTime.text.toString()
                        }
                        if(fullTime.isChecked){
                            workingMode += if(workingMode == "")
                                fullTime.text.toString()
                            else
                                ", " + fullTime.text.toString()
                        }
                        if(freelance.isChecked){
                            workingMode += if(workingMode == "")
                                freelance.text.toString()
                            else
                                ", " + freelance.text.toString()
                        }

                        if(chgImg){
                            //after changing profile pic
                            uploadImage { imageUrl ->
                                val userEdit = JobSeeker(fname, lname, email, gender.text.toString(), dob, nationality.text.toString(), contactNo, icno, imageUrl, country, state, salary, workingMode, user.status)
                                val imageName = loadedUrl?.substringAfterLast("%2F")?.substringBefore("?alt=")
                                db.collection("Job Seeker").document(email).set(userEdit)
                                val imgRef = FirebaseStorage.getInstance().getReference().child("Employee/$imageName")
                                imgRef.delete()
                                viewModel.setJobSeeker(userEdit)
                            }
                        }
                        else{
                            //no chg profile pic
                            val userEdit = JobSeeker(fname, lname, email, gender.text.toString(), dob, nationality.text.toString(), contactNo, icno, loadedUrl, country, state, salary, workingMode, user.status)
                            db.collection("Job Seeker").document(email).set(userEdit)
                            viewModel.setJobSeeker(userEdit)
                        }
                        requireView().findNavController().navigate(R.id.action_profileFragment_self)
                        Toast.makeText(requireContext(), "Successfully Edit Profile", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel"){dialogInterface,it->

                    }
                    .show()
            }
        }

        return binding.root
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

    private fun checkMode(){
        return if (!binding.myCheckBox1.isChecked && !binding.myCheckBox2.isChecked && !binding.myCheckBox3.isChecked) {
            binding.errorTextMode.visibility = View.VISIBLE
            binding.errorTextMode.text = "Please select your working mode"
            error = true
        } else {
            binding.errorTextMode.visibility = View.GONE
        }
    }

    private fun checkGender(genderGroup: RadioGroup):Boolean{
        return if (genderGroup.checkedRadioButtonId == -1) {
            binding.errorTextGender.visibility = View.VISIBLE
            binding.errorTextGender.text = "Please select your gender"
//            val radioButton = findViewById<RadioButton>(genderGroup.checkedRadioButtonId)
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
//            val radioButton = findViewById<RadioButton>(nationality.checkedRadioButtonId)
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

    private fun checkSalary(salary:String){
        if(salary.isEmpty()){
            binding.errorTextSalary.visibility = View.VISIBLE
            binding.errorTextSalary.text = "Please enter your expected salary"
            error = true
        }else{
            try {
                val number = salary.toInt()
                binding.errorTextSalary.visibility = View.GONE
            } catch (e: NumberFormatException) {
                binding.errorTextSalary.visibility = View.VISIBLE
                binding.errorTextSalary.text = "Please enter only numbers"
                error = true
            }
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

    private fun randomString(n: Int): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..n)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
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

    override fun onResume() {
        super.onResume()
        val activity = activity as Home
        activity?.showBottomNav()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            2404 -> if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                selectedImg = data?.data!!
                binding.uploadImage.setImageURI(selectedImg)
                chgImg = true

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.changePasswordFragment -> {
                NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                return true
            }
            R.id.logout -> {
                startActivity(Intent(requireContext(), Logout::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

}