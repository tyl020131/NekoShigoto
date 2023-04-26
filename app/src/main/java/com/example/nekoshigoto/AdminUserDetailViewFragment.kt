package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nekoshigoto.databinding.FragmentAdminUserDetailViewBinding
import com.example.nekoshigoto.databinding.FragmentAdminViewUserBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AdminUserDetailViewFragment : Fragment() {
    private lateinit var binding : FragmentAdminUserDetailViewBinding
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminUserDetailViewBinding.inflate(inflater, container, false)

        val userEmail = arguments?.getString("dataKey").toString()
        //binding.userName.text = userEmail.toString()
        db.collection("User").document(userEmail).get()
            .addOnSuccessListener {
                val user = it.toObject<UserDetailView>()
                val emailForDetailView = user?.email.toString()

                db.collection("Job Seeker").document(emailForDetailView).get()
                    .addOnSuccessListener {
                        val userDetail = it.toObject<JobSeeker>()
                        binding.userName.text = userDetail?.fname + " " + userDetail?.lname
                        binding.userIcNo.text = userDetail?.icPassport
                        binding.userEmail.text = userDetail?.email
                        binding.userContactNo.text = userDetail?.contactNo
                        binding.userGender.text = userDetail?.gender
                        binding.userCountry.text = userDetail?.country
                        binding.userState.text = userDetail?.state
                        //binding.userProfPic.setImageResource()
                    }

            }

        return binding.root
    }

}