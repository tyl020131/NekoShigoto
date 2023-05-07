package com.example.nekoshigoto

import JobAdapter
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.nekoshigoto.databinding.FragmentJobDetailBinding
import com.example.nekoshigoto.databinding.FragmentSubmitVacancyBinding
import com.google.firebase.firestore.FirebaseFirestore

class JobDetailFragment : Fragment() {

    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var view: FragmentJobDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        view = FragmentJobDetailBinding.inflate(layoutInflater)

        val jobid = arguments?.getString("jobname").toString()

        db.collection("Vacancy").document(jobid).get()
            .addOnSuccessListener { document ->
            if (document != null) {
                val vacancy = document.toObject<Vacancy>(Vacancy::class.java)
                if(vacancy!=null){
                    val imgUri = vacancy.image.toUri().buildUpon().scheme("https").build()
                    view.detailImg.load(imgUri)
                    view.detailPosition.text = vacancy.position
                    view.detailCompany.text = vacancy.companyName
                    view.detailMode.text = vacancy.mode
                    view.detailDescription.text = vacancy.description
                    view.detailImg.tag = vacancy.status
                    view.salary.text = "RM ${vacancy.salary}.00"
                }

            } else {
                Log.d(TAG, "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

        view.button4.setOnClickListener {
            val viewModel = ViewModelProvider(requireActivity()).get(JobSeekerViewModel::class.java)
            val jobSeeker = viewModel.getJobSeeker()
            val qualification = viewModel.getQualification()

            val jobStatus = view.detailImg.tag as String
            if(jobStatus=="Active"){
                db.collection("Vacancy").document(jobid)
                    .collection("Application").document(jobSeeker.email).get()
                    .addOnSuccessListener { snapshot ->
                        if (snapshot.exists() && snapshot.getString("status") != "R") {
                            Toast.makeText(requireContext(), "Application already submitted", Toast.LENGTH_SHORT).show()
                        }else {
                            if(qualification.field == "" && qualification.education == "" && qualification.workingExp=="") {
                                val dialog = AlertDialog.Builder(requireContext())

                                dialog.setTitle("Qualification Missing ")
                                    .setMessage("Do you want to submit qualification?")
                                    .setCancelable(true)
                                    .setPositiveButton("Go to qualification") { dialogInterface, _ ->
                                        val bundle = Bundle()
                                        bundle.putString("back", "back")
                                        it.findNavController().navigate(R.id.action_jobDetailFragment_to_qualificationFragment, bundle)
                                    }
                                    .setNegativeButton("Submit without qualification") { dialogInterface, _ ->
                                        val bundle = Bundle()
                                        bundle.putString("jobid", jobid)
                                        it.findNavController().navigate(R.id.action_jobDetailFragment_to_submitApplicationFragment, bundle)
                                    }
                                    .setNeutralButton("Cancel") { dialogInterface, _ ->
                                    }
                                    .show()
                            }
                            else{
                                val bundle = Bundle()
                                bundle.putString("jobid", jobid)
                                it.findNavController().navigate(R.id.action_jobDetailFragment_to_submitApplicationFragment, bundle)
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
            }else{
                Toast.makeText(requireContext(), "This job is not available right now", Toast.LENGTH_SHORT).show()
            }

        }

        return view.root;
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as Home
        activity?.hideBottomNav()
        activity?.chgTitle("Job Detail")
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