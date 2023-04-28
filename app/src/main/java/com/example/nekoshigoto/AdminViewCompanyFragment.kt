package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.databinding.FragmentAdminViewCompanyBinding
import com.example.nekoshigoto.databinding.FragmentAdminViewUserBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AdminViewCompanyFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var CompanyListForAdmin : ArrayList<CompanyView>
    private lateinit var binding : FragmentAdminViewCompanyBinding
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminViewCompanyBinding.inflate(inflater, container, false)

        binding.userViewBtn.setOnClickListener {
            findNavController().navigate(R.id.action_adminViewCompanyFragment2_to_adminViewUserFragment2)
        }


        val view = binding.root

        newRecyclerView = view.findViewById(R.id.companies)
        newRecyclerView.layoutManager = LinearLayoutManager(activity)
        newRecyclerView.setHasFixedSize(true)

        CompanyListForAdmin = arrayListOf<CompanyView>()
        loadData()

        binding.comfirmFilterBtn.setOnClickListener {
            val selectedFilterItem = binding.filterCategorySpinner.selectedItemPosition.toString()
            loadFilteredDate(selectedFilterItem)
        }

        return view

    }

    private fun loadData(){
        db.collection("Company").get()
            .addOnSuccessListener {
                for (document in it) {
                    val allCompany = document.toObject<CompanyView>()
                    if(allCompany?.status.toString() == "A"){
                        CompanyListForAdmin.add(CompanyView(allCompany?.name.toString(), "Active", allCompany?.email.toString()))
                    }
                    else if(allCompany?.status.toString() == "B"){
                        CompanyListForAdmin.add(CompanyView(allCompany?.name.toString(), "Blocked", allCompany?.email.toString()))
                    }
                }
                newRecyclerView.adapter = CompanyAdapterForAdmin(CompanyListForAdmin)
            }
    }

    private fun loadFilteredDate(selectedFilterItem : String){
        if(selectedFilterItem == "1") { //1 --> Active
            CompanyListForAdmin.clear()
            db.collection("Company").whereEqualTo("status", "A").get()
                .addOnSuccessListener {
                    for (document in it) {
                        val allCompany = document.toObject<CompanyView>()
                        CompanyListForAdmin.add(CompanyView(allCompany?.name.toString(), "Active", allCompany?.email.toString()))
                    }
                    newRecyclerView.adapter = CompanyAdapterForAdmin(CompanyListForAdmin)
                }
        }
        else if(selectedFilterItem == "2") { //2 --> Blocked
            CompanyListForAdmin.clear()
            db.collection("Company").whereEqualTo("status", "B").get()
                .addOnSuccessListener {
                    for (document in it) {
                        val allCompany = document.toObject<CompanyView>()
                        CompanyListForAdmin.add(CompanyView(allCompany?.name.toString(), "Blocked", allCompany?.email.toString()))
                    }
                    newRecyclerView.adapter = CompanyAdapterForAdmin(CompanyListForAdmin)
                }
        }
        else{ //0 --> All
            CompanyListForAdmin.clear()
            loadData()
        }
    }
}