package com.example.nekoshigoto

import FieldAdapter
import JobAdapter
import ModeAdapter
import VacancyAdapter
import android.app.Dialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.firebase.firestore.FirebaseFirestore


class HomeFragment : Fragment() {
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var jobList : ArrayList<Vacancy>
    private lateinit var djobList : ArrayList<Vacancy>

    lateinit var imageId : Array<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        imageId = arrayOf(
            R.drawable.kunkun
        )
        newRecyclerView = view.findViewById(R.id.jobs)
        newRecyclerView.layoutManager = LinearLayoutManager(activity);
        newRecyclerView.setHasFixedSize(true)

        jobList = arrayListOf<Vacancy>()
        loadData()

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


        return view;


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

        newRecyclerView.adapter = VacancyAdapter(filteredJobs)


    }


    private fun loadData(){




        db.collection("Vacancy")
            .whereEqualTo("companyName", "Ikun Studio")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    val vacancy = document.toObject(Vacancy::class.java)

                    jobList.add(vacancy)


                }
                newRecyclerView.adapter = JobAdapter(jobList)

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }


    }


}