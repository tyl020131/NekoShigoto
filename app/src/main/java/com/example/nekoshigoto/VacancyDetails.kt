package com.example.nekoshigoto

import UserAdapter
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nekoshigoto.databinding.FragmentVacancyDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject


class JobDetailsUser : Fragment() {
    private lateinit var binding: FragmentVacancyDetailsBinding
    private lateinit var newRecyclerView: RecyclerView
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var emailList : ArrayList<String>
    private lateinit var userList : ArrayList<JobSeeker>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVacancyDetailsBinding.inflate(inflater, container, false)
        var view = binding.root
        // Inflate the layout for this fragment

        val vacID = arguments?.getString("dataKey").toString()

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

        db.collection("Vacancy").document(vacID).collection("Application").get().addOnSuccessListener { documents->
            for(document in documents){
                var email = document.id
                emailList.add(email)
            }
        }


        return view
    }

    private fun loadData(){
        db.collection("Job Seeker")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val jobSeeker = document.toObject(JobSeeker::class.java)
                    if(emailList.contains(document.getString("email"))){
                        userList.add(jobSeeker)
                    }
                    if (userList.size == emailList.size) {
                        // Stop the loop once we have 10 users
                        break
                    }

                }
                newRecyclerView.adapter = VacancyDetailAdapter(userList)

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}