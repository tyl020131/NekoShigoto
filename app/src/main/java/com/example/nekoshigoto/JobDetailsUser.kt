package com.example.nekoshigoto

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager.TAG
import coil.load
import com.example.nekoshigoto.databinding.FragmentJobDetailsUserBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject


class JobDetailsUser : Fragment() {
    private lateinit var binding: FragmentJobDetailsUserBinding
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobDetailsUserBinding.inflate(inflater, container, false)
        var view = binding.root
        // Inflate the layout for this fragment

        val vacID = arguments?.getString("dataKey").toString()


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


        binding.button4.setOnClickListener {
            if (binding.button4.tag as String == "") {
                Toast.makeText(requireContext(), "Applied", Toast.LENGTH_SHORT).show()
            }


        }
        return view
    }
}
