package com.example.nekoshigoto

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nekoshigoto.databinding.FragmentVacancyDetailBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class VacancyDetailFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var emailList : ArrayList<String>
    private lateinit var statusList : ArrayList<String>
    private lateinit var userList: ArrayList<JobSeeker>
    private lateinit var appList: ArrayList<Applicant>
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
        newRecyclerView.setNestedScrollingEnabled(true);
        newRecyclerView.setHasFixedSize(true)
        var jobStatus = String()
        emailList = ArrayList<String>()
        statusList = ArrayList<String>()
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
                    jobStatus = vacancy?.status?:""
                    if(jobStatus=="Active")
                    {
                        binding.activeButton.setImageResource(R.drawable.deactivate)
                        binding.activeButton.setOnClickListener {
                            deactivate()
                        }
                    }
                    else
                    {
                        binding.activeButton.setImageResource(R.drawable.activate)
                        binding.activeButton.setOnClickListener {
                            activate()
                        }
                    }
                }
                db.collection("Vacancy").document(vacID).collection("Application").get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            if(document.getString("status")!="R"){
                                var email = document.id
                                var status = ""
                                if(document.getString("status") == "P"){
                                    status = "Pending"
                                }
                                else if(document.getString("status") == "A"){
                                    status = "Approved"
                                }
                                else
                                    status = "Rejected"

                                emailList.add(email)
                                statusList.add(status)
                            }
                        }
                        loadData()
                    }


            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }

        return view

    }

    private fun loadData() {
        userList = ArrayList<JobSeeker>()
        appList = ArrayList<Applicant>()

        db.collection("Job Seeker")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val jobSeeker = document.toObject(JobSeeker::class.java)
                    if (emailList.contains(document.getString("email"))) {
                        val position: Int = emailList.indexOf(document.getString("email"))
                        var status = statusList[position]
                        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        val dob = LocalDate.parse(jobSeeker.dob, formatter)
                        val currentDate: LocalDate = LocalDate.now()
                        val age: Int = Period.between(dob, currentDate).getYears()
                        val applicant = Applicant(jobSeeker.fname+" "+jobSeeker.lname, age, jobSeeker.profilePic, status, jobSeeker.email)
                        appList.add(applicant)
                        userList.add(jobSeeker)
                    }
                    else if (userList.size == emailList.size) {
                        // Stop the loop once we have 10 users
                        break
                    }

                }
                //newRecyclerView.adapter = VacancyDetailAdapter(userList,vacID)
                newRecyclerView.adapter = VacancyDetailAdapter(appList,vacID)

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    private fun activate(){
        val dialog = AlertDialog.Builder(requireContext())

        dialog.setTitle("Activate Confirmation")
            .setMessage("Do you want to activate the vacancy?")
            .setCancelable(true)
            .setPositiveButton("Activate") { dialogInterface, _ ->
                val query = db.collection("Vacancy").document(vacID)
                query.update("status", "Active").addOnSuccessListener {
                    Toast.makeText(requireContext(), "Successfully activate the vacancy", Toast.LENGTH_SHORT).show()
                    binding.activeButton.setImageResource(R.drawable.deactivate)
                    binding.activeButton.setOnClickListener {
                        deactivate()
                    }
                }
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->

            }

            .show()
    }

    private fun deactivate(){
        val dialog = AlertDialog.Builder(requireContext())

        dialog.setTitle("Deactivate Confirmation")
            .setMessage("Do you want to deactivate the vacancy?")
            .setCancelable(true)
            .setPositiveButton("Deactivate") { dialogInterface, _ ->
                val query = db.collection("Vacancy").document(vacID)
                query.update("status", "Inactive").addOnSuccessListener {
                    Toast.makeText(requireContext(), "Successfully deactivate the vacancy", Toast.LENGTH_SHORT).show()
                    binding.activeButton.setImageResource(R.drawable.activate)
                    binding.activeButton.setOnClickListener {
                        activate()
                    }
                }
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->

            }

            .show()
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as CompanyHome
        activity?.hideBottomNav()
        activity?.chgTitle("Vacancy Detail")
    }
}