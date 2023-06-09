package com.example.nekoshigoto

import FieldAdapter
import JobAdapter
import ModeAdapter
import SavedAdapter
import VacancyAdapter
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject


class VacancyFragment : Fragment() {
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var VacancyList : ArrayList<Vacancy>
    private lateinit var viewModel : CompanyViewModel
    private lateinit var navigator : NavController
    lateinit var imageId : Array<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        navigator = findNavController()
        viewModel = ViewModelProvider(requireActivity()).get(CompanyViewModel::class.java)
        var sh : SharedPreferences = requireActivity().getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)

        var name = sh.getString("name","").toString()


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_vacancy, container, false)

        val display_name = name.takeIf { it.length <= 25 } ?: name.take(25) + "..."
        val homeuser = view.findViewById<TextView>(R.id.home_username)
        homeuser.text = display_name
        imageId = arrayOf(
            R.drawable.kunkun
        )

        val homesearch = view.findViewById<EditText>(R.id.home_search)
        homesearch.addTextChangedListener {

            val newlist = ArrayList<Vacancy>()
            val text = homesearch.text.toString()
            for(job in VacancyList){
                if(job.position.contains(text,ignoreCase = true)){
                    newlist.add(job)
                }
            }
            Log.d(ContentValues.TAG,"hehehehehhbe")
            newRecyclerView.adapter =  VacancyAdapter(newlist,navigator)

        }
        newRecyclerView = view.findViewById(R.id.vacancies)
        newRecyclerView.layoutManager = LinearLayoutManager(activity);
        newRecyclerView.setHasFixedSize(true)

        VacancyList = arrayListOf<Vacancy>()
        loadData(name)

        val filterBtn : ImageButton = view.findViewById(R.id.filter_home)

        filterBtn.setOnClickListener {
            var myDialog : VacancyFilterDialog = VacancyFilterDialog(requireContext())
            myDialog.dialog.show()
            val filter : Button = myDialog.dialog.findViewById<Button>(R.id.filter_btn)
            filter.setOnClickListener {
                myDialog.updateField()
                val sort : String = myDialog.sort
                val salaryRange :ArrayList<Int> = myDialog.salary_range
                val fields = myDialog.fields
                val modes = myDialog.modes
                val gender = myDialog.gender
                val status = myDialog.status
                filterArray(gender,sort, salaryRange,fields, modes, status)
                myDialog.dialog.dismiss()
            }


        }

        val create_button : Button = view.findViewById(R.id.create_vacancy_btn)
        create_button.setOnClickListener {
            this.findNavController().navigate(R.id.action_vacancyFragment_to_submitVacancyFragment)
        }

        return view


    }

    private fun filterArray(gender:String,sort : String, salaryRange:ArrayList<Int>,fields:ArrayList<String>, modes:ArrayList<String>, status:String){

        val filteredJobs : ArrayList<Vacancy> = ArrayList<Vacancy>();


        VacancyList.forEach { vacancy->
            if(gender != "All")
            {
                //gender is male or female
                if(status != "All")
                {
                    //status is active or inactive
                    if(vacancy.gender == gender && vacancy.salary > salaryRange[0] && vacancy.salary< salaryRange[1] && vacancy.status == status){
                        if(fields.size!=0){
                            if(!fields.contains(vacancy.field)){
                                return@forEach

                            }
                        }
                        if(modes.size!=0){
                            if(modes.contains(vacancy.mode)){
                                filteredJobs.add(vacancy)
                            }
                        }
                        else{
                            filteredJobs.add(vacancy)
                        }
                        if(fields.contains(vacancy.field) && modes.contains(vacancy.mode)){
                            Log.d(ContentValues.TAG,"Filtered")

                        }
                    }
                    else{

                    }

                }
                else
                {
                    //status is all
                    if(vacancy.gender == gender && vacancy.salary > salaryRange[0] && vacancy.salary< salaryRange[1]){
                        if(fields.size!=0){
                            if(!fields.contains(vacancy.field)){
                                return@forEach

                            }
                        }
                        if(modes.size!=0){
                            if(modes.contains(vacancy.mode)){
                                filteredJobs.add(vacancy)
                            }
                        }
                        else{
                            filteredJobs.add(vacancy)
                        }
                        if(fields.contains(vacancy.field) && modes.contains(vacancy.mode)){
                            Log.d(ContentValues.TAG,"Filtered")

                        }
                    }
                    else{

                    }

                }
            }
            else
            {
                //gender is all
                if(status != "All")
                {
                    //status is active or inactive
                    if(vacancy.salary > salaryRange[0] && vacancy.salary< salaryRange[1] && vacancy.status == status){
                        if(fields.size!=0){
                            if(!fields.contains(vacancy.field)){
                                return@forEach

                            }
                        }
                        if(modes.size!=0){
                            if(modes.contains(vacancy.mode)){
                                filteredJobs.add(vacancy)
                            }
                        }
                        else{
                            filteredJobs.add(vacancy)
                        }
                        if(fields.contains(vacancy.field) && modes.contains(vacancy.mode)){
                            Log.d(ContentValues.TAG,"Filtered")

                        }
                    }
                    else{

                    }

                }
                else
                {
                    //status is all
                    if(vacancy.salary > salaryRange[0] && vacancy.salary< salaryRange[1]){
                        if(fields.size!=0){
                            if(!fields.contains(vacancy.field)){
                                return@forEach

                            }
                        }
                        if(modes.size!=0){
                            if(modes.contains(vacancy.mode)){
                                filteredJobs.add(vacancy)
                            }
                        }
                        else{
                            filteredJobs.add(vacancy)
                        }
                        if(fields.contains(vacancy.field) && modes.contains(vacancy.mode)){
                            Log.d(ContentValues.TAG,"Filtered")

                        }
                    }
                    else{

                    }
                }

            }
        }

        if(sort == "Salary High"){
            filteredJobs.sortByDescending { vacancy -> vacancy.salary }
        }
        else{
            filteredJobs.sortByDescending { vacancy -> vacancy.salary }
        }
        val sortedVacancyList = filteredJobs.sortedWith(compareBy(
            { if (it.status == "Active") 0 else if (it.status == "Inactive") 1 else 2 },
            { it.position }
        ))
        filteredJobs.clear()
        filteredJobs.addAll(sortedVacancyList)
        newRecyclerView.adapter = VacancyAdapter(filteredJobs,navigator)
    }



    private fun loadData(name:String) {
        db.collection("Vacancy")
            .whereEqualTo("companyName", name)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val vacancy = document.toObject(Vacancy::class.java)
                    if (document != null) {
                        vacancy.vacancyid = document.id
                        val query = db.collection("Vacancy").document("${document.id}").collection("Application")
                        query.get().addOnSuccessListener {
                            vacancy.numOfApp = it.size()
                            VacancyList.add(vacancy)
                            val sortedVacancyList = VacancyList.sortedWith(compareBy(
                                { if (it.status == "Active") 0 else if (it.status == "Inactive") 1 else 2 },
                                { it.position }
                            ))
                            VacancyList.clear()
                            VacancyList.addAll(sortedVacancyList)
                            newRecyclerView.adapter = VacancyAdapter(VacancyList,navigator)
                        }
                    }
                }

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
        }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.company_options, menu)
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
            R.id.companyEmailTemplateFragment -> {
                NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        val activity = activity as CompanyHome
        activity?.showBottomNav()
        activity?.chgTitle("My Company")
    }

}