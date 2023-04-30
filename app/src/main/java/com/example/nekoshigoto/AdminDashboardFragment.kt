package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nekoshigoto.databinding.FragmentAdminDashboardBinding
import com.example.nekoshigoto.databinding.FragmentAdminViewUserBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class AdminDashboardFragment : Fragment() {
    private lateinit var binding : FragmentAdminDashboardBinding
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)

        /*val userRef = db.collection("Job Seeker")
        val userCount = await getCountFromServer(userRef);*/

        val userRef = db.collection("User").whereEqualTo("userType", "jobSeeker")
        val userCount = runBlocking { getCountFromServer(userRef) }

        //val companyRef = db.collection("Company")
        val companyRef = db.collection("User").whereEqualTo("userType", "company")
        val companyCount = runBlocking { getCountFromServer(companyRef) }

        binding.totalUserNum.text = userCount.toString()
        binding.totalCompanyNum.text = companyCount.toString()

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        val activity = activity as AdminHome
        activity?.showBottomNav()
    }

    suspend fun getCountFromServer(query: Query): Long {
        val snapshot = query.get().await()

        return snapshot.size().toLong()
    }
}