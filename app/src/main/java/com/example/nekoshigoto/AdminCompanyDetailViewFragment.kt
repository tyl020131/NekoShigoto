package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
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
                    }
            }

        return binding.root
    }

}