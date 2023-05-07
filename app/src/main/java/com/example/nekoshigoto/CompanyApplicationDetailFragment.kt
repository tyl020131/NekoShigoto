package com.example.nekoshigoto

import VacancyAdapter
import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import coil.load
import com.example.nekoshigoto.databinding.FragmentCompanyApplicationDetailBinding
import com.example.nekoshigoto.databinding.FragmentVacancyDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter


class CompanyApplicationDetailFragment : Fragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var binding: FragmentCompanyApplicationDetailBinding
    private lateinit var vacID : String
    private lateinit var email : String
    private lateinit var phoneNo : String
    private val REQUEST_PHONE_CALL = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentCompanyApplicationDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        vacID = arguments?.getString("vacID").toString()
        email = arguments?.getString("email").toString()
        // Inflate the layout for this fragment

        db.collection("Vacancy").document(vacID).get()
            .addOnSuccessListener {
                val vacancy = it.toObject<Vacancy>()
                val query = db.collection("Vacancy").document("${it.id}").collection("Application")
                query.get().addOnSuccessListener {
                    vacancy?.numOfApp = it.size()
                    binding.apply {
                        val imgUri = vacancy?.image?.toUri()?.buildUpon()?.scheme("https")?.build()
                        binding.jobPic.load(imgUri)
                        companyName.text = vacancy?.companyName
                        vacancyName.text = vacancy?.position
                        noOfApply.text = "${vacancy?.numOfApp} people has applied for this vacancy"
                        mode.text = vacancy?.mode

                    }
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }

        db.collection("Vacancy").document(vacID).collection("Application").document(email).get()
            .addOnSuccessListener {
                val app = it.toObject<AppDetails>()
                binding.apply {
                    coverLetter.text = app?.coverLetter
                    button11.tag = app?.resumeUrl
                    if(app?.status!="P")
                    {
                        binding.reject.visibility=View.GONE
                        binding.approve.visibility=View.GONE
                    }
                }

                }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }

        db.collection("Job Seeker").document(email)
            .get()
            .addOnSuccessListener {
                val jobSeeker = it.toObject(JobSeeker::class.java)
                if(jobSeeker!=null){
                    binding.jobb.setOnClickListener {
                        val bundle = bundleOf("dataKey" to email)
                        it.findNavController().navigate(R.id.action_companyApplicationDetailFragment_to_userDetailFragment, bundle)
                    }

                    phoneNo = jobSeeker.contactNo.replace("-", "")

                    val imgUri = jobSeeker.profilePic.toUri().buildUpon().scheme("https").build()
                    binding.profilepic.load(imgUri)
                    val name = jobSeeker.fname + " " + jobSeeker.lname
                    binding.name.text = name

                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val dob = LocalDate.parse(jobSeeker.dob, formatter)
                    val currentDate: LocalDate = LocalDate.now()
                    val age: Int = Period.between(dob, currentDate).getYears()
                    binding.age.text = "$age Years Old, ${jobSeeker.nationality}"

                    binding.location.text = jobSeeker.state + ", " + jobSeeker.country

                    var list = jobSeeker.workingMode.split(", ")
                    list.forEach {
                        if(it == binding.freelance.text.toString())
                            binding.freelance.visibility = View.VISIBLE
                        else if(it == binding.partTime.text.toString())
                            binding.partTime.visibility = View.VISIBLE
                        else if(it == binding.fullTime.text.toString())
                            binding.fullTime.visibility = View.VISIBLE

                    }
                }


            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

        binding.button11.setOnClickListener {
            if(binding.button11.tag as String == "")
            {
                Toast.makeText(requireContext(), "No resume has been uploaded", Toast.LENGTH_SHORT).show()
            }
            else{
                viewPdf()
            }

        }

        binding.approve.setOnClickListener {

            val dialog = AlertDialog.Builder(requireContext())
            val eDialog = AlertDialog.Builder(requireContext())
            dialog.setTitle("Approve Confirmation")
                .setMessage("Do you want to approve this application?")
                .setCancelable(true)
                .setPositiveButton("Approve") { dialogInterface, _ ->
                    val query = db.collection("Vacancy").document(vacID).collection("Application").document(email)
                    query.update("status", "A").addOnSuccessListener {
                        Toast.makeText(requireContext(), "Successfully approve the application", Toast.LENGTH_SHORT).show()

                        eDialog.setTitle("Contact applicant")
                            .setMessage("Successfully approved, do you want to contact the applicant?")
                            .setCancelable(true)
                            .setPositiveButton("Email") { dialogInterface, _ ->
                                sendEmail(email)

                            }
                            .setNegativeButton("Phone Call") { dialogInterface, _ ->
                                onClickMakePhoneCall()
                            }
                            .setNeutralButton("Cancel"){ dialogInterface, _ ->
                                val activity = activity as CompanyHome?
                                activity?.onSupportNavigateUp()
                            }
                            .show()

                    }
                }
                .setNegativeButton("Cancel") { dialogInterface, _ ->

                }

                .show()
        }

        binding.reject.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())

            dialog.setTitle("Reject Confirmation")
                .setMessage("Do you want to reject this application?")
                .setCancelable(true)
                .setPositiveButton("Reject") { dialogInterface, _ ->
                    val query = db.collection("Vacancy").document(vacID).collection("Application").document(email)
                    query.update("status", "R").addOnSuccessListener {
                        Toast.makeText(requireContext(), "Successfully reject the application", Toast.LENGTH_SHORT).show()
                        val activity = activity as CompanyHome?
                        activity?.onSupportNavigateUp()
                    }
                }
                .setNegativeButton("Cancel") { dialogInterface, _ ->

                }

                .show()
        }

        return view
    }
    override fun onResume() {
        super.onResume()
        val activity = activity as CompanyHome
        activity?.hideBottomNav()
        activity?.chgTitle("Application of vacancy")
    }

    fun onClickMakePhoneCall() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
        } else {
            // Permission already granted, make the phone call
            makePhoneCall(phoneNo)
        }
    }

        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == REQUEST_PHONE_CALL) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, make phone call
                    makePhoneCall(phoneNo)
                } else {
                    // Permission is not granted, show a message to the user
                    Toast.makeText(requireContext(), "Phone call permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun makePhoneCall(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(callIntent)

        val activity = activity as CompanyHome?
        activity?.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.company_options, menu)
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
            R.id.companyEmailTemplateFragment -> {
                NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun viewPdf(){
        var loadedUrl = binding.button11.tag as String
        val pdf = loadedUrl?.substringAfterLast("%2F")?.substringBefore("?alt=")
        val pdfRef = FirebaseStorage.getInstance().getReference().child("Resume/$pdf")
        val localFile = File.createTempFile(pdf, ".pdf")

        pdfRef.getFile(localFile)
            .addOnSuccessListener {
                val fileUri = FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".fileprovider", localFile)

                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(fileUri, "application/pdf")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            }
            .addOnFailureListener {

            }
    }

    private fun sendEmail(email: String){
        val viewModel = ViewModelProvider(requireActivity()).get(CompanyViewModel::class.java)
        val company = viewModel.getCompany().name
        val sendEmail = email
        db.collection("Company").document(viewModel.getCompany().email).collection("Email").document("Approve").get()
            .addOnSuccessListener {
                val subject = it.getString("subject")
                val message = it.getString("body") +
                        "\n\nThank you.\n\nBest regards,\n$company"

                val emailIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(sendEmail))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, message)

                    //setPackage("com.google.android.gm") // set Gmail as the email app
                }

                startActivity(Intent.createChooser(emailIntent, "Send email using"))

                val activity = activity as CompanyHome?
                activity?.onSupportNavigateUp()
            }

    }
}