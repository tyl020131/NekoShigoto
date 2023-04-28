package com.example.nekoshigoto

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import coil.load
import com.example.nekoshigoto.databinding.FragmentCompanyApplicationDetailBinding
import com.example.nekoshigoto.databinding.FragmentVacancyDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject


class CompanyApplicationDetailFragment : Fragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var emailList: ArrayList<String>
    private lateinit var userList: ArrayList<JobSeeker>
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

                binding.apply {
                    val imgUri = vacancy?.image?.toUri()?.buildUpon()?.scheme("https")?.build()
                    binding.jobPic.load(imgUri)
                    companyName.text = vacancy?.companyName
                    vacancyName.text = vacancy?.position
                    location.text = vacancy?.location
                    mode.text = vacancy?.mode
                    mode2.text = vacancy?.mode

                }

            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }

        db.collection("Vacancy").document(vacID).collection("Application").document(email).get()
            .addOnSuccessListener {
                val vacancy = it.toObject<AppDetails>()
                binding.apply {
                    coverLetter.text = vacancy?.coverLetter
                }

                }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }

        db.collection("Job Seeker").document(email)
            .get()
            .addOnSuccessListener {
                val jobSeeker = it.toObject(JobSeeker::class.java)
                binding.apply {
                    applicantNamee.text = jobSeeker?.fname+" "+ jobSeeker?.lname
                    val imgUri = jobSeeker?.profilePic?.toUri()?.buildUpon()?.scheme("https")?.build()
                    binding.profilepic1.load(imgUri)
                    location1.text = jobSeeker?.state
                }

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

        db.collection("Vacancy").document(vacID).get()
            .addOnSuccessListener {
                val vacancy = it.toObject<Vacancy>()
                binding.apply {
                    position.text = vacancy?.position
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }

        return view
    }

}