package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.databinding.FragmentAdminViewBinding
import com.example.nekoshigoto.databinding.FragmentAdminViewUserBinding

class AdminViewUserFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var userListForAdmin : ArrayList<UserView>
    private lateinit var binding : FragmentAdminViewUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminViewUserBinding.inflate(inflater, container, false)

        binding.companyViewBtn.setOnClickListener {
            findNavController().navigate(R.id.action_adminViewUserFragment2_to_adminViewCompanyFragment2)
        }

        val view = binding.root

        newRecyclerView = view.findViewById(R.id.users)
        newRecyclerView.layoutManager = LinearLayoutManager(activity)
        newRecyclerView.setHasFixedSize(true)

        userListForAdmin = arrayListOf<UserView>()
        loadData()

        return view

    }

    private fun loadData(){
        userListForAdmin.add(UserView("PaoPao", "Active", "test@gmail.com"))
        userListForAdmin.add(UserView("NickNick", "Inactive", "banana@gmail.com"))
        userListForAdmin.add(UserView("PaoPao", "Active", "happy@gmail.com"))
        userListForAdmin.add(UserView("NickNick", "Inactive", "banana@gmail.com"))
        userListForAdmin.add(UserView("PaoPao", "Active", "happy@gmail.com"))
        userListForAdmin.add(UserView("NickNick", "Inactive", "banana@gmail.com"))
        userListForAdmin.add(UserView("PaoPao", "Active", "happy@gmail.com"))
        userListForAdmin.add(UserView("NickNick", "Inactive", "banana@gmail.com"))
        userListForAdmin.add(UserView("PaoPao", "Active", "happy@gmail.com"))
        userListForAdmin.add(UserView("NickNick", "Inactive", "banana@gmail.com"))
        userListForAdmin.add(UserView("PaoPao", "Active", "happy@gmail.com"))
        userListForAdmin.add(UserView("NickNick", "Inactive", "banana@gmail.com"))
        userListForAdmin.add(UserView("PaoPao", "Active", "happy@gmail.com"))
        userListForAdmin.add(UserView("NickNick", "Inactive", "banana@gmail.com"))
        userListForAdmin.add(UserView("PaoPao", "Active", "happy@gmail.com"))
        userListForAdmin.add(UserView("NickNick", "Inactive", "banana@gmail.com"))
        userListForAdmin.add(UserView("PaoPao", "Active", "happy@gmail.com"))
        userListForAdmin.add(UserView("NickNick", "Inactive", "banana@gmail.com"))

        newRecyclerView.adapter = UserAdapterForAdmin(userListForAdmin)


    }

}