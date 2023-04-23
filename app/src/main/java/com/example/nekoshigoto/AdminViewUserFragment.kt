package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.databinding.FragmentAdminViewUserBinding

class AdminViewUserFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var userListForAdmin : ArrayList<UserView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_view_user, container, false)

        newRecyclerView = view.findViewById(R.id.users)
        newRecyclerView.layoutManager = LinearLayoutManager(activity)
        newRecyclerView.setHasFixedSize(true)

        userListForAdmin = arrayListOf<UserView>()
        loadData()

        return view

    }

    private fun loadData(){
        userListForAdmin.add(UserView("PaoPao", "Active"))
        userListForAdmin.add(UserView("NickNick", "Inactive"))

        newRecyclerView.adapter = UserAdapterForAdmin(userListForAdmin)


    }

}