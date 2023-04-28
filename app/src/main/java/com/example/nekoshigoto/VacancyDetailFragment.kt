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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nekoshigoto.databinding.FragmentVacancyDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class VacancyDetailFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var emailList: ArrayList<String>
    private lateinit var userList: ArrayList<JobSeeker>
    private lateinit var binding: FragmentVacancyDetailBinding
    private lateinit var vacID : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVacancyDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        vacID = arguments?.getString("jobname").toString()
        newRecyclerView = binding.vacDetailRecycle
        newRecyclerView.layoutManager = LinearLayoutManager(activity);
        newRecyclerView.setHasFixedSize(true)

        emailList = ArrayList<String>()
//        db.collection("Vacancy").document(vacID).collection("Application").get()
        db.collection("Vacancy").document(vacID).get()
            .addOnSuccessListener {
                val vacancy = it.toObject<Vacancy>()

                binding.apply {
                    val imgUri = vacancy?.image?.toUri()?.buildUpon()?.scheme("https")?.build()
                    binding.jobImage.load(imgUri)
                    textDesc.text = vacancy?.description
                    jobPosition.text = vacancy?.position
                    tag.text = vacancy?.gender
                    tag1.text = vacancy?.mode

                }

            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }

        db.collection("Vacancy").document(vacID).collection("Application").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var email = document.id
                    emailList.add(email)
                }
            }
        loadData()
        return view

    }

    private fun loadData() {
        userList = ArrayList<JobSeeker>()

        db.collection("Job Seeker")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val jobSeeker = document.toObject(JobSeeker::class.java)
                    if (emailList.contains(document.getString("email"))) {
                        userList.add(jobSeeker)
                    }
                    else if (userList.size == emailList.size) {
                        // Stop the loop once we have 10 users
                        break
                    }

                }
                newRecyclerView.adapter = VacancyDetailAdapter(userList,vacID)

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}