package com.example.nekoshigoto

import FieldAdapter
import FilterJobSeekerDialog
import JobAdapter
import ModeAdapter
import UserAdapter
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.databinding.FragmentViewUserBinding
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter


class ViewUserFragment : Fragment() {
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var binding : FragmentViewUserBinding
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var userList : ArrayList<JobSeeker>
    private lateinit var dialogRecyclerView: RecyclerView
    private lateinit var modeList : ArrayList<String>
    private lateinit var fieldRecyclerView: RecyclerView
    private lateinit var fieldList : ArrayList<String>
    lateinit var imageId : Array<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentViewUserBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        val view = binding.root

        imageId = arrayOf(
            R.drawable.kunkun
        )
        newRecyclerView = binding.vacancies
        newRecyclerView.layoutManager = LinearLayoutManager(activity);
        newRecyclerView.setHasFixedSize(true)

        userList = arrayListOf<JobSeeker>()
        loadData()

        val filterBtn : ImageButton = binding.filterHome

        filterBtn.setOnClickListener {
            var myDialog : FilterJobSeekerDialog = FilterJobSeekerDialog(requireContext())
            myDialog.dialog.show()
            val filter : Button = myDialog.dialog.findViewById<Button>(R.id.filter_btn)
            filter.setOnClickListener {
                myDialog.updateField()
                val ageRange :ArrayList<Int> = myDialog.age_range
                val modes = myDialog.modes
                val gender = myDialog.gender
                val nationality = myDialog.nationality
                filterArray(gender, ageRange, modes, nationality)
                myDialog.dialog.dismiss()
            }
        }

        binding.homeSeeall.setOnClickListener{
            newRecyclerView.adapter = UserAdapter(userList)
        }

        return view
    }

    private fun filterArray(gender:String, ageRange:ArrayList<Int>, modes:ArrayList<String>, nationality:String){

        val filteredUser : ArrayList<JobSeeker> = ArrayList<JobSeeker>();

        userList.forEach { user->
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val dob = LocalDate.parse(user.dob, formatter)
            val currentDate: LocalDate = LocalDate.now()
            val age: Int = Period.between(dob, currentDate).getYears()
            if(user.gender == gender && age > ageRange[0] && age < ageRange[1] && user.nationality == nationality){

                if(modes.size!=0){
                    var list = user.workingMode.split(", ")
//                    modes.forEach{ mode ->
//                        list.forEach {
//                            if(it == mode)
//                                filteredUser.add(user)
//                        }
//                    }
                    list.forEach{
                        if(modes.contains(it)){
                            filteredUser.add(user)
                            return@forEach // break out of the loop
                        }
                    }

//                    if(modes.contains(user.workingMode)){
//                        filteredUser.add(user)
//                    }
                }
                else{
                    Log.d(ContentValues.TAG,"Added")
                    filteredUser.add(user)
                }
            }else{
                Log.d(ContentValues.TAG,"fail to add")
            }
        }

        newRecyclerView.adapter = UserAdapter(filteredUser)


    }

    private fun loadData(){
        db.collection("Job Seeker")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    val jobSeeker = document.toObject(JobSeeker::class.java)

                    userList.add(jobSeeker)
                }
                newRecyclerView.adapter = UserAdapter(userList)

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
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

    override fun onResume() {
        super.onResume()
        val activity = activity as CompanyHome
        activity?.showBottomNav()
    }
}