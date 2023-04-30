package com.example.nekoshigoto

import ApplicationAdapter
import UserAdapter
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.databinding.FragmentActivityBinding
import com.example.nekoshigoto.databinding.FragmentViewUserBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter


class ActivityFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var binding : FragmentActivityBinding
    private lateinit var appAdapter : ApplicationAdapter
    private lateinit var appList : ArrayList<Application>
    private lateinit var user : JobSeeker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        binding = FragmentActivityBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(requireActivity()).get(JobSeekerViewModel::class.java)
        user = viewModel.getJobSeeker()

        val view = binding.root

        newRecyclerView = binding.applications
        newRecyclerView.layoutManager = LinearLayoutManager(activity);
        newRecyclerView.setHasFixedSize(true)
        val myList = ArrayList<Application>()
        appAdapter = ApplicationAdapter(myList)
        newRecyclerView.adapter = appAdapter

        appList = arrayListOf<Application>()
        loadData()

        val filterBtn : ImageButton = binding.filterHome
        val seeall = binding.homeSeeall

        filterBtn.setOnClickListener {
            val filteredApp : ArrayList<Application> = ArrayList<Application>()
            val choice = arrayOf<CharSequence>("Pending", "Approved", "Rejected", "Cancel")
            val myAlertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            myAlertDialog.setTitle("Select Image")
            myAlertDialog.setCancelable(true)
            myAlertDialog.setItems(choice, DialogInterface.OnClickListener { dialog, item ->
                if(choice[item] != "Cancel"){
                    appList.forEach { app->
                        if(app.status == choice[item])
                            filteredApp.add(app)
                    }
                    appAdapter = ApplicationAdapter(filteredApp)
                    newRecyclerView.adapter = appAdapter
                }
            })
            myAlertDialog.show()
        }

        seeall.setOnClickListener{
            appAdapter = ApplicationAdapter(appList)
            newRecyclerView.adapter = appAdapter
        }

        return view;


    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.changePasswordFragment -> {
                NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                return true
            }
            R.id.logout -> {
                startActivity(Intent(requireContext(), Logout::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadData(){
        db.collection("Vacancy")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("Vacancy").document(document.id).collection("Application")
                        .document(user.email).get()
                        .addOnSuccessListener { app ->
                            if(app.exists()){
                                val image = document.getString("image")?:""
                                Log.d("test", "test")
                                val company = document.getString("companyName")?:""
                                Log.d("status", app.getString("status")?:"")

                                val vacancy = document.getString("position")?:""
                                val location = document.getString("location")?:""
                                val mode = document.getString("mode")?:""
                                var status = ""
                                if(app.getString("status") == "A")
                                    status = "Approved"
                                else if (app.getString("status") == "P")
                                    status = "Pending"
                                else
                                    status = "Rejected"

                                var application = Application(document.id, image, company, vacancy, location, mode, status)
                                appList.add(application)
                            }
                            appAdapter = ApplicationAdapter(appList)
                            newRecyclerView.adapter = appAdapter
                        }
                }


            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as Home
        activity?.showBottomNav()
    }

}