package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.databinding.FragmentAdminViewBinding
import com.example.nekoshigoto.databinding.FragmentAdminViewUserBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AdminViewUserFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var userListForAdmin : ArrayList<UserView>
    private lateinit var binding : FragmentAdminViewUserBinding
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

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

        binding.comfirmFilterBtn.setOnClickListener {
            val selectedFilterItem = binding.filterCategorySpinner.selectedItemPosition.toString()
            loadFilteredDate(selectedFilterItem)
        }

        return view
    }

    private fun loadData(){
        db.collection("Job Seeker").get()
            .addOnSuccessListener {
                for (document in it) {
                    val name = document.getString("fname") + " " + document.get("lname")
                    val allJobSeeker = document.toObject<UserView>()
                    if(allJobSeeker?.status.toString() == "A"){
                        userListForAdmin.add(UserView(name, "Active", allJobSeeker?.email.toString()))
                    }
                    else{ //if user blocked
                        userListForAdmin.add(UserView(name, "Blocked", allJobSeeker?.email.toString()))
                    }

                }
                newRecyclerView.adapter = UserAdapterForAdmin(userListForAdmin)
            }

    }


    private fun loadFilteredDate(selectedFilterItem : String){
        if(selectedFilterItem == "1"){ //1 --> Active
            userListForAdmin.clear()
            db.collection("Job Seeker").whereEqualTo("status", "A").get()
                .addOnSuccessListener {
                    for (document in it) {
                        val name = document.getString("fname") + " " + document.get("lname")
                        val allJobSeeker = document.toObject<UserView>()
                        userListForAdmin.add(UserView(name, "Active", allJobSeeker?.email.toString()))
                    }
                    newRecyclerView.adapter = UserAdapterForAdmin(userListForAdmin)
                }
        }
        else if(selectedFilterItem == "2"){ //2 --> Blocked
            userListForAdmin.clear()
            db.collection("Job Seeker").whereEqualTo("status", "B").get()
                .addOnSuccessListener {
                    for (document in it) {
                        val name = document.getString("fname") + " " + document.get("lname")
                        val allJobSeeker = document.toObject<UserView>()
                        userListForAdmin.add(UserView(name, "Blocked", allJobSeeker?.email.toString()))
                    }
                    newRecyclerView.adapter = UserAdapterForAdmin(userListForAdmin)
                }
        }
        else{ //0 --> All
            userListForAdmin.clear()
            loadData()
        }
    }
}