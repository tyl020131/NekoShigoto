package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminCompanyRequestFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var CompanyRequestListForAdmin : ArrayList<CompanyRequest>

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
        CompanyRequestListForAdmin.add(CompanyRequest("Wong", "Active"))
        CompanyRequestListForAdmin.add(CompanyRequest("Zhi", "Inactive"))
        CompanyRequestListForAdmin.add(CompanyRequest("hi", "Active"))
        CompanyRequestListForAdmin.add(CompanyRequest("Zi", "Inactive"))
        CompanyRequestListForAdmin.add(CompanyRequest("zh", "Active"))

        newRecyclerView.adapter = CompanyRequestAdapterForAdmin(CompanyRequestListForAdmin)


    }
}