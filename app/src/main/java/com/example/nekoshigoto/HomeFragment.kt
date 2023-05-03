package com.example.nekoshigoto

import JobAdapter
import android.content.Intent
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() {
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var navigator : NavController
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var jobList : ArrayList<Vacancy>
    private lateinit var djobList : ArrayList<Vacancy>
    private lateinit var viewModel: JobSeekerViewModel
    private lateinit var jobAdapter : JobAdapter
    private var mysaved : ArrayList<Save> = ArrayList<Save>()
    private var email : String= ""


    lateinit var imageId : Array<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Disable the up button
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(requireActivity()).get(JobSeekerViewModel::class.java)
        var sh : SharedPreferences = requireActivity().getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)
        var email = sh.getString("userid","").toString()

        navigator = findNavController()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        imageId = arrayOf(
            R.drawable.kunkun
        )



        val homesearch = view.findViewById<EditText>(R.id.home_search)
        homesearch.addTextChangedListener {
            if(jobAdapter!=null){

                val newlist = ArrayList<Vacancy>()
                val text = homesearch.text.toString()
                for(job in jobList){
                    if(job.position.contains(text,ignoreCase = true)){
                        newlist.add(job)
                    }
                }
                Log.d(TAG,"hehehehehhbe")
                jobAdapter = JobAdapter(newlist, email,mysaved,navigator)
                newRecyclerView.adapter = jobAdapter

            }

        }
        newRecyclerView = view.findViewById(R.id.jobs)
        newRecyclerView.layoutManager = LinearLayoutManager(activity);
        newRecyclerView.setHasFixedSize(true)

        jobList = arrayListOf<Vacancy>()

        loadData(email)

        val filterBtn : ImageButton = view.findViewById(R.id.filter_home)

        filterBtn.setOnClickListener {
            var myDialog : FilterJobDialog = FilterJobDialog(requireContext())
            myDialog.dialog.show()
            val filter : Button = myDialog.dialog.findViewById<Button>(R.id.filter_btn)
            filter.setOnClickListener {
                myDialog.updateField()
                val sort : String = myDialog.sort
                val salaryRange :ArrayList<Float> = myDialog.salary_range
                val fields = myDialog.fields
                val modes = myDialog.modes
                val gender = myDialog.gender
                filterArray(gender,sort, salaryRange,fields, modes)
                myDialog.dialog.dismiss()
            }


        }

        val seeall : TextView = view.findViewById(R.id.home_seeall)

        seeall.setOnClickListener{
            jobAdapter = JobAdapter(jobList, email,mysaved,navigator)
            newRecyclerView.adapter = jobAdapter
        }

        return view
    }

    private fun filterArray(gender:String,sort : String, salaryRange:ArrayList<Float>,fields:ArrayList<String>, modes:ArrayList<String>){

        val filteredJobs : ArrayList<Vacancy> = ArrayList<Vacancy>();

        jobList.forEach { vacancy->
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
                    Log.d(TAG,"Filtered")

                }
            }
            else{

            }
        }

        if(sort == "Salary High"){
            filteredJobs.sortByDescending { vacancy -> vacancy.salary }
        }
        else{
            filteredJobs.sortByDescending { vacancy -> vacancy.salary }
        }

        jobAdapter = JobAdapter(jobList, email,mysaved,navigator)
        newRecyclerView.adapter = jobAdapter
    }


    private fun loadData(email:String){

        mysaved = ArrayList<Save>()
        db.collection("Job Seeker").document(email).collection("saves")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    val save = document.toObject(Save::class.java)
                    mysaved.add(save)
                }

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

        db.collection("Vacancy")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    val vacancy = document.toObject(Vacancy::class.java)
                    if(vacancy.status=="Active") {
                        vacancy.vacancyid = document.id
                        jobList.add(vacancy)
                    }
                }
                jobAdapter = JobAdapter(jobList, email,mysaved,navigator)
                newRecyclerView.adapter = jobAdapter

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }


    }

    override fun onResume() {
        super.onResume()
        val activity = activity as Home
        activity?.showBottomNav()
        activity?.chgTitle("My Home")
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


}