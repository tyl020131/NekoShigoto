package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.databinding.FragmentAdminViewCompanyBinding

class AdminViewCompanyFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var CompanyListForAdmin : ArrayList<CompanyView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_view_company, container, false)

        newRecyclerView = view.findViewById(R.id.companies)
        newRecyclerView.layoutManager = LinearLayoutManager(activity)
        newRecyclerView.setHasFixedSize(true)

        CompanyListForAdmin = arrayListOf<CompanyView>()
        loadData()

        return view

    }

    private fun loadData(){
        CompanyListForAdmin.add(CompanyView("PaoPao", "Active"))
        CompanyListForAdmin.add(CompanyView("NickNick", "Inactive"))
        CompanyListForAdmin.add(CompanyView("The wong", "Active"))

        newRecyclerView.adapter = CompanyAdapterForAdmin(CompanyListForAdmin)


    }
}