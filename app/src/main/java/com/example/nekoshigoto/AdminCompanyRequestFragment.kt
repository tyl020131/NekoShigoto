package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.databinding.FragmentAdminCompanyRequestBinding
import com.example.nekoshigoto.databinding.FragmentAdminViewCompanyBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AdminCompanyRequestFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var CompanyRequestListForAdmin : ArrayList<CompanyRequest>
    private lateinit var binding : FragmentAdminCompanyRequestBinding
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var tempName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminCompanyRequestBinding.inflate(inflater, container, false)

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

        newRecyclerView = view.findViewById(R.id.companyRequest)
        newRecyclerView.layoutManager = LinearLayoutManager(activity)
        newRecyclerView.setHasFixedSize(true)

        CompanyRequestListForAdmin = arrayListOf<CompanyRequest>()
        loadData()

        return view
    }

    private fun loadData(){
        CompanyRequestListForAdmin.clear()
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

    private fun loadSearchData(query : String){
        if(query == ""){
            loadData()
        }
        else{
            CompanyRequestListForAdmin.clear()
            db.collection("Company").get()
                .addOnSuccessListener {
                    for (document in it) {
                        val name = document.getString("name")
                        if(name?.uppercase()?.replace("\\s".toRegex(), "") == query.uppercase().replace("\\s".toRegex(), "")){
                            val allCompany = document.toObject<CompanyView>()
                            if(allCompany?.status.toString() == "P"){
                                CompanyRequestListForAdmin.add(CompanyRequest(name, "Pending", allCompany?.email.toString()))
                            }
                        }
                    }
                    newRecyclerView.adapter = CompanyRequestAdapterForAdmin(CompanyRequestListForAdmin)
                }
        }
    }
    override fun onResume() {
        super.onResume()
        val activity = activity as AdminHome
        activity?.showBottomNav()
    }

}