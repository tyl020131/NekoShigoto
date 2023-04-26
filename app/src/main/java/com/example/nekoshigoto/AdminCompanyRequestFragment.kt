package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AdminCompanyRequestFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var CompanyRequestListForAdmin : ArrayList<CompanyRequest>
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_company_request, container, false)

        newRecyclerView = view.findViewById(R.id.companyRequest)
        newRecyclerView.layoutManager = LinearLayoutManager(activity)
        newRecyclerView.setHasFixedSize(true)

        CompanyRequestListForAdmin = arrayListOf<CompanyRequest>()
        loadData()

        return view
    }

    private fun loadData(){
        db.collection("Company").get()
            .addOnSuccessListener {
                for (document in it) {
                    val allCompanyReq = document.toObject<CompanyRequest>()
                    if(allCompanyReq?.status.toString() == "P"){
                        CompanyRequestListForAdmin.add(CompanyRequest(allCompanyReq?.name.toString(), "Pending", allCompanyReq?.email.toString()))
                    }
                }
                newRecyclerView.adapter = CompanyRequestAdapterForAdmin(CompanyRequestListForAdmin)
            }

    }
}