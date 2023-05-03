package com.example.nekoshigoto

import VacancyAdapter
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.net.toUri
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            viewPdf()
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

                        eDialog.setTitle("Send Email")
                            .setMessage("Successfully approved, do you want to send an email to the applicant?")
                            .setCancelable(true)
                            .setPositiveButton("Yes") { dialogInterface, _ ->
                                sendEmail(email)
                            }
                            .setNegativeButton("No") { dialogInterface, _ ->
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
        activity?.chgTitle("Company Application Detail")
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
        val company = "Ikun Studio"
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
        val activity = activity as CompanyHome?
        activity?.onSupportNavigateUp()
    }
}