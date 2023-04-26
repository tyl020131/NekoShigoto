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

class AdminViewCompanyFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var CompanyListForAdmin : ArrayList<CompanyView>
    private lateinit var binding : FragmentAdminViewCompanyBinding

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

        return view

    }

    private fun loadData(){
        CompanyListForAdmin.add(CompanyView("PaoPao", "Active", "jinitaimei@gmail.com"))
        CompanyListForAdmin.add(CompanyView("NickNick", "Inactive", "amagi@gmail.com"))
        CompanyListForAdmin.add(CompanyView("The wong", "Active", "niganma@gmail.com"))

        newRecyclerView.adapter = CompanyAdapterForAdmin(CompanyListForAdmin)


    }
}