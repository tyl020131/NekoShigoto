package com.example.nekoshigoto

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.NumberFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.nekoshigoto.databinding.FragmentUserDetailBinding
import com.example.testemail.SendEmail
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*

//import javax.mail.MessagingException

class UserDetailFragment : Fragment() {
    private lateinit var binding : FragmentUserDetailBinding
    private lateinit var storage : FirebaseStorage
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var dialog : AlertDialog.Builder
    private lateinit var viewModel : CompanyViewModel
    private lateinit var phoneNo : String
    private val REQUEST_PHONE_CALL = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(requireActivity()).get(CompanyViewModel::class.java)
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        binding.downloadCVButton.tag = ""
        val email = arguments?.getString("dataKey").toString()
        db.collection("Job Seeker").document(email).get()
            .addOnSuccessListener{
                val user = it.toObject<JobSeeker>()
                binding.apply {
                    Glide.with(requireContext())
                        .load(user?.profilePic)
                        .into(profilePic)
                    phoneNo = user?.contactNo!!.replace("-", "")
                    nameTextView.text = user?.fname + " " + user?.lname
                    textEmail.text = email
                    textDob.text = user?.dob
                    textGender.text = user?.gender
                    textNationality.text = user?.nationality
                    textAddress.text = user?.state + ", " + user?.country
                    val number = user?.salary
                    val formattedNumber = NumberFormat.getNumberInstance(Locale.US).format(number)
                    textSalary.text = "RM $formattedNumber.00"

                    val list = user?.workingMode?.split(", ")
                    list?.forEach {
                        if(it == binding.freelance.text.toString())
                            binding.freelance.visibility = View.VISIBLE
                        else if(it == binding.partTime.text.toString())
                            binding.partTime.visibility = View.VISIBLE
                        else if(it == binding.fullTime.text.toString())
                            binding.fullTime.visibility = View.VISIBLE
                    }
                }

            }.addOnFailureListener {
                Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
            }

        db.collection("Qualification").document(email).get()
            .addOnSuccessListener{
                val qualification = it.toObject<Qualification>()
                binding.apply {
                    if(qualification == null) {
                        linearLayout3.visibility = View.GONE
                        textView36.text = "Qualification Not Found"
                    }else{
                        textField.text = qualification.field
                        textEducation.text = qualification.education
                        textExp.text = qualification.workingExp
                        downloadCVButton.tag = qualification.resumeURl
                    }

                }

            }.addOnFailureListener {
                Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
            }

        binding.downloadCVButton.setOnClickListener {
            if(binding.downloadCVButton.tag as String == ""){
                Toast.makeText(requireContext(), "No resume has been uploaded", Toast.LENGTH_SHORT).show()
            }else{
                viewPdf()
            }
        }

        binding.approachButton.setOnClickListener {

            val eDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            eDialog.setTitle("Approach applicant")
                .setMessage("Please select a method to approach user")
                .setCancelable(true)
                .setPositiveButton("Email") { dialogInterface, _ ->
                    val company = viewModel.getCompany().name
                    val sendEmail = email
                    db.collection("Company").document(viewModel.getCompany().email).collection("Email").document("Approach").get()
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
                        }

                }
                .setNegativeButton("Phone Call") { dialogInterface, _ ->
                    onClickMakePhoneCall()
                }
                .setNeutralButton("Cancel"){ dialogInterface, _ ->
                }
                .show()


        }

        return binding.root
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
    }

    private fun viewPdf(){
        var loadedUrl = binding.downloadCVButton.tag as String
        val oldPDF = loadedUrl?.substringAfterLast("%2F")?.substringBefore("?alt=")
        val pdfRef = FirebaseStorage.getInstance().getReference().child("Resume/$oldPDF")
        val localFile = File.createTempFile(oldPDF, ".pdf")

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

    override fun onResume() {
        super.onResume()
        val activity = activity as CompanyHome
        activity?.hideBottomNav()
        activity?.chgTitle("User Profile Detail")
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
}