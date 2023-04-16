package com.example.nekoshigoto

//import VacancyDetailAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VacancyDetailFragment : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var ApplicantList : ArrayList<Applicant>
    lateinit var imageId : Array<Int>

    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_vacancy_detail, container, false)

        imageId = arrayOf(
            R.drawable.kunkun
        )
        newRecyclerView = view.findViewById(R.id.applicants)
        //newRecyclerView.layoutManager = LinearLayoutManager(activity);
        newRecyclerView.setHasFixedSize(true)

        ApplicantList = arrayListOf<Applicant>()
        loadData()

        return view;


    }

    private fun loadData(){
        ApplicantList.add(Applicant("Wong", 10, imageId[0]))
        ApplicantList.add(Applicant("Zhi", 20, imageId[0]))

        newRecyclerView.adapter = VacancyDetailAdapter(ApplicantList)
    }
}