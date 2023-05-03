package com.example.nekoshigoto

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.nekoshigoto.databinding.FragmentCompanyEmailTemplateBinding
import com.google.firebase.firestore.FirebaseFirestore

class CompanyEmailTemplateFragment : Fragment() {
    private lateinit var binding : FragmentCompanyEmailTemplateBinding
    private lateinit var viewModel : CompanyViewModel
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(CompanyViewModel::class.java)
        val company = viewModel.getCompany()
        binding = FragmentCompanyEmailTemplateBinding.inflate(inflater, container, false)
        val view = binding.root



        val subject = "Selected as Potential Candidate"
        val message = "Greeting from ${company.name}, we are impressed with " +
                "your profile and would like to express our interest in " +
                "you as a potential candidate for a job opportunity, " +
                "kindly respond to this email so that we can arrange an " +
                "interview with you"

        binding.apply {
            db.collection("Company").document(company.email).collection("Email").document("Approach").get()
                .addOnSuccessListener{
                    editTextApproachBody.setText(it.getString("body").toString())
                    editTextApproachSubject.setText(it.getString("subject").toString())
            }
            db.collection("Company").document(company.email).collection("Email").document("Approve").get().addOnSuccessListener {
                editTextApproveBody.setText(it.getString("body").toString())
                editTextApproveSubject.setText(it.getString("subject").toString())
            }

            saveBtn.setOnClickListener {
                val app1body = editTextApproachBody.text
                val app1subject = editTextApproachSubject.text
                val app2body = editTextApproveBody.text
                val app2subject = editTextApproveSubject.text

                val dialog = AlertDialog.Builder(requireContext())

                dialog.setTitle("Submit template ")
                    .setMessage("Are you sure to submit the current email template? (Empty field will be saved as default template)")
                    .setCancelable(true)
                    .setPositiveButton("Submit"){dialogInterface,it->
                        val approachQuery = db.collection("Company").document(company.email).collection("Email").document("Approach")

                        if(app1subject.toString()!="")
                            approachQuery.update("subject", app1subject.toString())
                        else{
                            approachQuery.update("subject", subject)
                            editTextApproachSubject.setText(subject)
                        }

                        if(app1body.toString()!="")
                            approachQuery.update("body", app1body.toString())
                        else{
                            approachQuery.update("body", message)
                            editTextApproachBody.setText(message)
                        }


                        val approveQuery = db.collection("Company").document(company.email).collection("Email").document("Approve")

                        if(app2subject.toString()!="")
                            approveQuery.update("subject", app2subject.toString())
                        else{
                            approveQuery.update("subject", subject)
                            editTextApproveSubject.setText(subject)
                        }

                        if(app2body.toString()!="")
                            approveQuery.update("body", app2body.toString())
                        else{
                            editTextApproveBody.setText(message)
                            approveQuery.update("body", message)
                        }

                        Toast.makeText(requireContext(), "Successfully updated your template", Toast.LENGTH_LONG).show()

                    }.setNegativeButton("Cancel"){ dialogInterface,it->

                    }.show()
            }

            removeButton.setOnClickListener{
                val dialog = AlertDialog.Builder(requireContext())

                dialog.setTitle("Submit template ")
                    .setMessage("Are you sure to set the current email template back to default?")
                    .setCancelable(true)
                    .setPositiveButton("Default"){dialogInterface,it->
                        val approachQuery = db.collection("Company").document(company.email).collection("Email").document("Approach")
                        approachQuery.update("subject", subject)
                        approachQuery.update("body", message)

                        val approveQuery = db.collection("Company").document(company.email).collection("Email").document("Approve")
                        approveQuery.update("subject", subject)
                        approveQuery.update("body", message)

                        Toast.makeText(requireContext(), "Successfully updated your template", Toast.LENGTH_LONG).show()
                        editTextApproveBody.setText(message)
                        editTextApproveSubject.setText(subject)
                        editTextApproachSubject.setText(subject)
                        editTextApproachBody.setText(message)

                    }.setNegativeButton("Cancel"){ dialogInterface,it->

                    }.show()
            }
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as CompanyHome
        activity?.hideBottomNav()
        activity?.chgTitle("Update Email Template")
    }

}