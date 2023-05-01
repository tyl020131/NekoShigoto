package com.example.nekoshigoto

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.nekoshigoto.databinding.FragmentAdminProfileBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField

class AdminProfileFragment : Fragment() {

    private lateinit var binding : FragmentAdminProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminProfileBinding.inflate(inflater, container, false)
        binding.editTextTextAdminEmail.isEnabled = false
        binding.editTextTextAdminName.isEnabled = false
        sharedPreferences = requireContext().getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)

        var email = sharedPreferences.getString("userid", "").toString()

        db.collection("Admin").get()
            .addOnSuccessListener { documents->
                for(document in documents)
                {
                    if(document.getString("email") == email){
                        binding.editTextTextAdminName.setText(document.getString("username"))
                        binding.editTextTextAdminEmail.setText(email)
                        break
                    }
                }
            }
            .addOnFailureListener {
                Log.e("error", it.message.toString())
            }

        binding.button7.setOnClickListener {
            val intent = Intent(requireActivity(), Logout::class.java)
            startActivity(intent)
        }

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        val activity = activity as AdminHome
        activity?.showBottomNav()
    }

}