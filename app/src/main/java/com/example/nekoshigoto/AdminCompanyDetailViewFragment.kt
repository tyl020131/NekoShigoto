package com.example.nekoshigoto

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.nekoshigoto.databinding.FragmentAdminCompanyDetailViewBinding
import com.example.nekoshigoto.databinding.FragmentAdminUserDetailViewBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AdminCompanyDetailViewFragment : Fragment() {
    private lateinit var binding : FragmentAdminCompanyDetailViewBinding
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminCompanyDetailViewBinding.inflate(inflater, container, false)
        val companyEmail = arguments?.getString("dataKey").toString()

        binding.closeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_adminCompanyDetailViewFragment_to_adminViewCompanyFragment2)
        }
        //binding.companyName.text = companyEmail.toString()
        db.collection("User").document(companyEmail).get()
            .addOnSuccessListener {
                val company = it.toObject<CompanyDetailView>()
                val emailForDetailView = company?.email.toString()

                db.collection("Company").document(emailForDetailView).get()
                    .addOnSuccessListener {
                        val companyDetail = it.toObject<Company>()
                        binding.companyName.text = companyDetail?.name
                        binding.companyBusiness.text = companyDetail?.business
                        binding.companyEmail.text = companyDetail?.email
                        binding.companyAddress.text = companyDetail?.address
                        binding.companyContactNo.text = companyDetail?.contactNo
                        binding.companyCountry.text = companyDetail?.country
                        binding.companyState.text = companyDetail?.state

                        val imgUri = companyDetail?.profilePic?.toUri()?.buildUpon()?.scheme("https")?.build()
                        binding.companyPic.load(imgUri)

                        if(companyDetail?.status.toString() == "A"){ //if company is active
                            binding.decisionBtn.setText("Block")
                            binding.decisionBtn.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.color8)));
                        }
                        else{
                            binding.decisionBtn.setText("Activate") //if company is blocked
                            binding.decisionBtn.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.color9)));
                        }
                    }
            }

        binding.decisionBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            if(binding.decisionBtn.text == "Block"){
                builder.setTitle("Block Company")
                builder.setMessage("Do you want to BLOCK this company?")

                builder.setPositiveButton("OK") { dialog, which ->
                    db.collection("User").document(companyEmail).update("status", "B")
                        .addOnSuccessListener {
                            db.collection("Company").document(companyEmail).update("status", "B")
                                .addOnSuccessListener {
                                    val bundle = bundleOf("dataKey" to companyEmail)
                                    view?.findNavController()?.navigate(R.id.action_adminCompanyDetailViewFragment_self, bundle)
                                    Toast.makeText(context, "Company Blocked Successfully!", Toast.LENGTH_SHORT).show()
                                }
                        }
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    val bundle = bundleOf("dataKey" to companyEmail)
                    view?.findNavController()?.navigate(R.id.action_adminCompanyDetailViewFragment_self, bundle)
                }
            }
            else{
                builder.setTitle("Activate Company")
                builder.setMessage("Do you want to ACTIVATE this company?")

                builder.setPositiveButton("OK") { dialog, which ->
                    db.collection("User").document(companyEmail).update("status", "A")
                        .addOnSuccessListener {
                            db.collection("Company").document(companyEmail).update("status", "A")
                                .addOnSuccessListener {
                                    val bundle = bundleOf("dataKey" to companyEmail)
                                    view?.findNavController()?.navigate(R.id.action_adminCompanyDetailViewFragment_self, bundle)
                                    Toast.makeText(context, "Company Activated Successfully!", Toast.LENGTH_SHORT).show()
                                }
                        }
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    val bundle = bundleOf("dataKey" to companyEmail)
                    view?.findNavController()?.navigate(R.id.action_adminCompanyDetailViewFragment_self, bundle)
                }
            }

            val dialog = builder.create()
            dialog.show()
        }

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        val activity = activity as AdminHome
        activity?.hideBottomNav()
    }

}