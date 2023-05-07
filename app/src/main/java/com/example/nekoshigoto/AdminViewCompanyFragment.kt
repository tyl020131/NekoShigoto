package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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
    private var tempName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminViewCompanyBinding.inflate(inflater, container, false)

        binding.userViewBtn.setOnClickListener {
            findNavController().navigate(R.id.action_adminViewCompanyFragment2_to_adminViewUserFragment2)
        }

        binding.searchCompany.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //binding.textView22.text = query
                tempName = query.toString()
                loadSearchData(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //binding.textView22.text = newText
                tempName = newText.toString()
                loadSearchData(newText.toString())
                return true
            }
        })

        val view = binding.root

        newRecyclerView = view.findViewById(R.id.companies)
        newRecyclerView.layoutManager = LinearLayoutManager(activity)
        newRecyclerView.setHasFixedSize(true)

        CompanyListForAdmin = arrayListOf<CompanyView>()
        loadData()

        binding.comfirmFilterBtn.setOnClickListener {
            val selectedFilterItem = binding.filterCategorySpinner.selectedItemPosition.toString()
            if(tempName != "")
                loadFilteredDateWithSearch(selectedFilterItem, tempName)
            else
                loadFilteredDate(selectedFilterItem)
        }

        return view

    }

    private fun loadData(){
        CompanyListForAdmin.clear()
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

    private fun loadDateWithSearchValue(tempName: String){
        CompanyListForAdmin.clear()
        db.collection("Company").get()
            .addOnSuccessListener {
                for (document in it) {
                    val name = document.getString("name")
                    if(name?.uppercase()?.replace("\\s".toRegex(), "") == tempName.uppercase().replace("\\s".toRegex(), "")){
                        val allCompany = document.toObject<CompanyView>()
                        if(allCompany?.status.toString() == "A"){
                            CompanyListForAdmin.add(CompanyView(name, "Active", allCompany?.email.toString()))
                        }
                        else{ //if user blocked
                            CompanyListForAdmin.add(CompanyView(name, "Blocked", allCompany?.email.toString()))
                        }
                    }
                }
                newRecyclerView.adapter = CompanyAdapterForAdmin(CompanyListForAdmin)
            }
    }

    private fun loadSearchData(query : String){
        if(query == ""){
            loadData()
        }
        else{
            CompanyListForAdmin.clear()
            db.collection("Company").get()
                .addOnSuccessListener {
                    for (document in it) {
                        val name = document.getString("name")
                        if(name?.uppercase()?.replace("\\s".toRegex(), "") == query.uppercase().replace("\\s".toRegex(), "")){
                            val allCompany = document.toObject<CompanyView>()
                            if(allCompany?.status.toString() == "A"){
                                CompanyListForAdmin.add(CompanyView(name, "Active", allCompany?.email.toString()))
                            }
                            else{ //if user blocked
                                CompanyListForAdmin.add(CompanyView(name, "Blocked", allCompany?.email.toString()))
                            }
                        }
                    }
                    newRecyclerView.adapter = CompanyAdapterForAdmin(CompanyListForAdmin)
                }
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
            loadData()
        }
    }

    private fun loadFilteredDateWithSearch(selectedFilterItem : String, tempName : String){
        if(selectedFilterItem == "1"){ //1 --> Active
            CompanyListForAdmin.clear()
            db.collection("Company").get().addOnSuccessListener {
                for (document in it) {
                    val status = document.getString("status")
                    val name = document.getString("name")
                    if(name?.uppercase()?.replace("\\s".toRegex(), "") == tempName.uppercase().replace("\\s".toRegex(), "") && status.toString() == "A"){
                        val allCompany = document.toObject<CompanyView>()
                        CompanyListForAdmin.add(CompanyView(name, "Active", allCompany?.email.toString()))
                    }
                }
                newRecyclerView.adapter = CompanyAdapterForAdmin(CompanyListForAdmin)
            }
        }
        else if(selectedFilterItem == "2"){ //2 --> Blocked
            CompanyListForAdmin.clear()
            db.collection("Company").get().addOnSuccessListener {
                for (document in it) {
                    val status = document.getString("status")
                    val name = document.getString("name")
                    if(name?.uppercase()?.replace("\\s".toRegex(), "") == tempName.uppercase().replace("\\s".toRegex(), "") && status.toString() == "B"){
                        val allCompany = document.toObject<CompanyView>()
                        CompanyListForAdmin.add(CompanyView(name, "Blocked", allCompany?.email.toString()))
                    }
                }
                newRecyclerView.adapter = CompanyAdapterForAdmin(CompanyListForAdmin)
            }
        }
        else{ //0 --> All
            loadDateWithSearchValue(tempName)
        }
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as AdminHome
        activity?.hideBottomNav()
    }
}