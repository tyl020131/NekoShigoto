package com.example.nekoshigoto

import android.app.AlertDialog
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
import com.example.nekoshigoto.databinding.FragmentAdminCompanyRequestDetailViewBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AdminCompanyRequestDetailViewFragment : Fragment() {
    private lateinit var binding : FragmentAdminCompanyRequestDetailViewBinding
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminCompanyRequestDetailViewBinding.inflate(inflater, container, false)

        val companyEmail = arguments?.getString("dataKey").toString()

        binding.closeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_adminCompanyRequestDetailViewFragment_to_adminCompanyRequestFragment2)
        }
        //binding.textView17.text = companyEmail

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
                    }
            }

        binding.approveBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Approve Company")
            builder.setMessage("Do you want to APPROVE this company?")

            builder.setPositiveButton("OK") { dialog, which ->
                db.collection("User").document(companyEmail).update("status", "A")
                    .addOnSuccessListener {
                        db.collection("Company").document(companyEmail).update("status", "A")
                            .addOnSuccessListener {
                                val bundle = bundleOf("dataKey" to companyEmail)
                                findNavController().navigate(R.id.action_adminCompanyRequestDetailViewFragment_to_adminCompanyRequestFragment2)
                                Toast.makeText(context, "Company APPROVED Successfully!", Toast.LENGTH_SHORT).show()
                            }
                    }
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                val bundle = bundleOf("dataKey" to companyEmail)
                view?.findNavController()?.navigate(R.id.action_adminCompanyRequestDetailViewFragment_self, bundle)
            }

            val dialog = builder.create()
            dialog.show()
        }

        binding.rejectBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Reject Company")
            builder.setMessage("Do you want to REJECT this company?")

            builder.setPositiveButton("OK") { dialog, which ->
                db.collection("User").document(companyEmail).get()
                    .addOnSuccessListener {
                        it.reference.delete()
                }
                db.collection("Company").document(companyEmail).get()
                    .addOnSuccessListener {
                        it.reference.delete()
                    }
                findNavController().navigate(R.id.action_adminCompanyRequestDetailViewFragment_to_adminCompanyRequestFragment2)
                Toast.makeText(context, "Company REJECTED Successfully!", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                val bundle = bundleOf("dataKey" to companyEmail)
                view?.findNavController()?.navigate(R.id.action_adminCompanyRequestDetailViewFragment_self, bundle)
            }

            val dialog = builder.create()
            dialog.show()
        }

        return binding.root
    }

}