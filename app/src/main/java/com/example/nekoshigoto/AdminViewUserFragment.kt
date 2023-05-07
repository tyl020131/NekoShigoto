package com.example.nekoshigoto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private var tempName = ""
    private var count = 0
    private var savedQuery: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminViewUserBinding.inflate(inflater, container, false)
        if (savedInstanceState != null) {
            savedQuery = savedInstanceState.getString("QUERY")
        }
        binding.companyViewBtn.setOnClickListener {
            findNavController().navigate(R.id.action_adminViewUserFragment2_to_adminViewCompanyFragment2)
        }

        binding.searchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //binding.textView22.text = query
                tempName = query.toString()
                loadSearchData(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                count++
                if(count > 1){
                    val query = newText.toString().trim()
                    if (query != savedQuery) {
                        savedQuery = query
                        tempName = newText.toString()
                        loadSearchData(query)
                    }
                }
                return true
            }
        })

        val view = binding.root

        newRecyclerView = view.findViewById(R.id.users)
        newRecyclerView.layoutManager = LinearLayoutManager(activity)
        newRecyclerView.setHasFixedSize(true)

        userListForAdmin = arrayListOf<UserView>()
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
        userListForAdmin.clear()
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

    private fun loadDateWithSearchValue(tempName: String){
        userListForAdmin.clear()
        db.collection("Job Seeker").get()
            .addOnSuccessListener {
                for (document in it) {
                    val fname = document.getString("fname")
                    val lname = document.getString("lname")
                    val name = document.getString("fname") + " " + document.getString("lname")
                    if(name.uppercase().replace("\\s".toRegex(), "") == tempName.uppercase().replace("\\s".toRegex(), "")){
                        val allJobSeeker = document.toObject<UserView>()
                        if(allJobSeeker?.status.toString() == "A"){
                            userListForAdmin.add(UserView(name, "Active", allJobSeeker?.email.toString()))
                        }
                        else{ //if user blocked
                            userListForAdmin.add(UserView(name, "Blocked", allJobSeeker?.email.toString()))
                        }
                    }
                    else if(fname?.uppercase()?.replace("\\s".toRegex(), "") == tempName.uppercase().replace("\\s".toRegex(), "")){
                        val allJobSeeker = document.toObject<UserView>()
                        if(allJobSeeker?.status.toString() == "A"){
                            userListForAdmin.add(UserView(name, "Active", allJobSeeker?.email.toString()))
                        }
                        else{ //if user blocked
                            userListForAdmin.add(UserView(name, "Blocked", allJobSeeker?.email.toString()))
                        }
                    }
                    else if(lname?.uppercase()?.replace("\\s".toRegex(), "") == tempName.uppercase().replace("\\s".toRegex(), "")){
                        val allJobSeeker = document.toObject<UserView>()
                        if(allJobSeeker?.status.toString() == "A"){
                            userListForAdmin.add(UserView(name, "Active", allJobSeeker?.email.toString()))
                        }
                        else{ //if user blocked
                            userListForAdmin.add(UserView(name, "Blocked", allJobSeeker?.email.toString()))
                        }
                    }
                }
                newRecyclerView.adapter = UserAdapterForAdmin(userListForAdmin)
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("QUERY", savedQuery)
    }

    private fun loadSearchData(query : String){
        if(query == ""){
            loadData()
        }
        else{
            userListForAdmin.clear()
            db.collection("Job Seeker").get()
                .addOnSuccessListener {
                    for (document in it) {
                        val fname = document.getString("fname")
                        val lname = document.getString("lname")
                        val name = document.getString("fname") + " " + document.getString("lname")
                        if(name.uppercase().replace("\\s".toRegex(), "") == query.uppercase().replace("\\s".toRegex(), "")){
                            val allJobSeeker = document.toObject<UserView>()
                            if(allJobSeeker?.status.toString() == "A"){
                                userListForAdmin.add(UserView(name, "Active", allJobSeeker?.email.toString()))
                            }
                            else{ //if user blocked
                                userListForAdmin.add(UserView(name, "Blocked", allJobSeeker?.email.toString()))
                            }
                        }
                        else if(fname?.uppercase()?.replace("\\s".toRegex(), "") == query.uppercase().replace("\\s".toRegex(), "")){
                            val allJobSeeker = document.toObject<UserView>()
                            if(allJobSeeker?.status.toString() == "A"){
                                userListForAdmin.add(UserView(name, "Active", allJobSeeker?.email.toString()))
                            }
                            else{ //if user blocked
                                userListForAdmin.add(UserView(name, "Blocked", allJobSeeker?.email.toString()))
                            }
                        }
                        else if(lname?.uppercase()?.replace("\\s".toRegex(), "") == query.uppercase().replace("\\s".toRegex(), "")){
                            val allJobSeeker = document.toObject<UserView>()
                            if(allJobSeeker?.status.toString() == "A"){
                                userListForAdmin.add(UserView(name, "Active", allJobSeeker?.email.toString()))
                            }
                            else{ //if user blocked
                                userListForAdmin.add(UserView(name, "Blocked", allJobSeeker?.email.toString()))
                            }
                        }
                    }
                    newRecyclerView.adapter = UserAdapterForAdmin(userListForAdmin)
                }
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
            loadData()
        }
    }

    private fun loadFilteredDateWithSearch(selectedFilterItem : String, tempName : String){
        if(selectedFilterItem == "1"){ //1 --> Active
            userListForAdmin.clear()
            db.collection("Job Seeker").get().addOnSuccessListener {
                for (document in it) {
                    val status = document.getString("status")
                    val fname = document.getString("fname")
                    val lname = document.getString("lname")
                    val name = document.getString("fname") + " " + document.getString("lname")
                    if(name.uppercase().replace("\\s".toRegex(), "") == tempName.uppercase().replace("\\s".toRegex(), "") && status.toString() == "A"){
                        val allJobSeeker = document.toObject<UserView>()
                        userListForAdmin.add(UserView(name, "Active", allJobSeeker?.email.toString()))
                    }
                    else if(fname?.uppercase()?.replace("\\s".toRegex(), "") == tempName.uppercase().replace("\\s".toRegex(), "") && status.toString() == "A"){
                        val allJobSeeker = document.toObject<UserView>()
                        userListForAdmin.add(UserView(name, "Active", allJobSeeker?.email.toString()))
                    }
                    else if(lname?.uppercase()?.replace("\\s".toRegex(), "") == tempName.uppercase().replace("\\s".toRegex(), "") && status.toString() == "A"){
                        val allJobSeeker = document.toObject<UserView>()
                        userListForAdmin.add(UserView(name, "Active", allJobSeeker?.email.toString()))
                    }
                }
                newRecyclerView.adapter = UserAdapterForAdmin(userListForAdmin)
            }
        }
        else if(selectedFilterItem == "2"){ //2 --> Blocked
            userListForAdmin.clear()
            db.collection("Job Seeker").get().addOnSuccessListener {
                for (document in it) {
                    val status = document.getString("status")
                    val fname = document.getString("fname")
                    val lname = document.getString("lname")
                    val name = document.getString("fname") + " " + document.getString("lname")
                    if(name.uppercase().replace("\\s".toRegex(), "") == tempName.uppercase().replace("\\s".toRegex(), "") && status.toString() == "B"){
                        val allJobSeeker = document.toObject<UserView>()
                        userListForAdmin.add(UserView(name, "Blocked", allJobSeeker?.email.toString()))
                    }
                    else if(fname?.uppercase()?.replace("\\s".toRegex(), "") == tempName.uppercase().replace("\\s".toRegex(), "") && status.toString() == "B"){
                        val allJobSeeker = document.toObject<UserView>()
                        userListForAdmin.add(UserView(name, "Blocked", allJobSeeker?.email.toString()))
                    }
                    else if(lname?.uppercase()?.replace("\\s".toRegex(), "") == tempName.uppercase().replace("\\s".toRegex(), "") && status.toString() == "B"){
                        val allJobSeeker = document.toObject<UserView>()
                        userListForAdmin.add(UserView(name, "Blocked", allJobSeeker?.email.toString()))
                    }
                }
                newRecyclerView.adapter = UserAdapterForAdmin(userListForAdmin)
            }
        }
        else{ //0 --> All
            loadDateWithSearchValue(tempName)
        }
    }
    override fun onResume() {
        super.onResume()
        val activity = activity as AdminHome
        activity?.showBottomNav()
    }
}