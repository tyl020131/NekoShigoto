package com.example.nekoshigoto

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.nekoshigoto.databinding.FragmentUserDetailBinding
import com.example.testemail.SendEmail
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import javax.mail.MessagingException

class UserDetailFragment : Fragment() {
    private lateinit var binding : FragmentUserDetailBinding
    private lateinit var storage : FirebaseStorage
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var dialog : AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        val email = "tankc2002@gmail.com"
        db.collection("Job Seeker").document(email).get()
            .addOnSuccessListener{
                val user = it.toObject<JobSeeker>()
                binding.apply {
                    Glide.with(requireContext())
                        .load(user?.profilePic)
                        .into(profilePic)

                    nameTextView.text = user?.fname + " " + user?.lname
                    textEmail.text = email
                    textDob.text = user?.dob
                    textGender.text = user?.gender
                    textNationality.text = user?.nationality
                    textAddress.text = user?.state + ", " + user?.country

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
                    textField.text = qualification?.field
                    textEducation.text = qualification?.education
                    textExp.text = qualification?.workingExp
                    downloadCVButton.tag = qualification?.resumeURl
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
//            try {
//                SendEmail(requireActivity()).execute("tankc2002@gmail.com", "This is from Neko Shigoto\nHappy Happy Happy", "Weak Chicken")
//
//            } catch (e: MessagingException) {
//                Log.e("fail", e.stackTraceToString())
//            }
            val company = "Ikun Studio"
            val email = "khaichin2002@gmail.com"
            val subject = "Selected as Potential Candidate"
            val message = "Greeting from $company, we are impressed with " +
                    "your profile and would like to express our interest in " +
                    "you as a potential candidate for a job opportunity, " +
                    "kindly respond to this email so that we can arrange an " +
                    "interview with you\n\nThank you.\n\nBest regards,\n$company"

            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)

                //setPackage("com.google.android.gm") // set Gmail as the email app
            }

            startActivity(Intent.createChooser(emailIntent, "Send email using"))
        }

        return binding.root
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
}