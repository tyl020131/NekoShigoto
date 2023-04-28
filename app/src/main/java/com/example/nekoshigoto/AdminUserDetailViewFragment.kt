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

        binding.closeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_adminUserDetailViewFragment2_to_adminViewUserFragment2)
        }
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

                        val imgUri = userDetail?.profilePic?.toUri()?.buildUpon()?.scheme("https")?.build()
                        binding.userProfPic.load(imgUri)

                        if(userDetail?.status.toString() == "A"){ //if user is active
                            binding.decisionBtn.setText("Block")
                            binding.decisionBtn.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.color8)));
                        }
                        else{
                            binding.decisionBtn.setText("Activate") //if user is blocked
                            binding.decisionBtn.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.color9)));
                        }
                    }

            }

        binding.decisionBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            if(binding.decisionBtn.text == "Block"){
                builder.setTitle("Block User")
                builder.setMessage("Do you want to BLOCK this user?")

                builder.setPositiveButton("OK") { dialog, which ->
                    db.collection("User").document(userEmail).update("status", "B")
                        .addOnSuccessListener {
                            db.collection("Job Seeker").document(userEmail).update("status", "B")
                                .addOnSuccessListener {
                                    val bundle = bundleOf("dataKey" to userEmail)
                                    view?.findNavController()?.navigate(R.id.action_adminUserDetailViewFragment2_self, bundle)
                                    Toast.makeText(context, "User Blocked Successfully!", Toast.LENGTH_SHORT).show()
                                }
                        }
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    val bundle = bundleOf("dataKey" to userEmail)
                    view?.findNavController()?.navigate(R.id.action_adminUserDetailViewFragment2_self, bundle)
                }
            }
            else{
                builder.setTitle("Activate User")
                builder.setMessage("Do you want to ACTIVATE this user?")

                builder.setPositiveButton("OK") { dialog, which ->
                    db.collection("User").document(userEmail).update("status", "A")
                        .addOnSuccessListener {
                            db.collection("Job Seeker").document(userEmail).update("status", "A")
                                .addOnSuccessListener {
                                    val bundle = bundleOf("dataKey" to userEmail)
                                    view?.findNavController()?.navigate(R.id.action_adminUserDetailViewFragment2_self, bundle)
                                    Toast.makeText(context, "User Activated Successfully!", Toast.LENGTH_SHORT).show()
                                }
                        }
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    val bundle = bundleOf("dataKey" to userEmail)
                    view?.findNavController()?.navigate(R.id.action_adminUserDetailViewFragment2_self, bundle)
                }
            }

            val dialog = builder.create()
            dialog.show()
        }

        return binding.root
    }

}