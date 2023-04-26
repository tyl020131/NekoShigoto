package com.example.nekoshigoto

import JobAdapter
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
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
                }

            } else {
                Log.d(TAG, "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

        return view.root;


    }
}