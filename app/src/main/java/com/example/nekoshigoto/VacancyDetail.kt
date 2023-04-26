package com.example.nekoshigoto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VacancyDetail : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var ApplicantList : ArrayList<Applicant>
    lateinit var imageId : Array<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacancy_detail)

        imageId = arrayOf(
            R.drawable.kunkun
        )

        newRecyclerView = findViewById(R.id.applicants)
        newRecyclerView.layoutManager = LinearLayoutManager(this);
        newRecyclerView.setHasFixedSize(true)

        ApplicantList = arrayListOf<Applicant>()
        loadData()
    }

    private fun loadData(){
        ApplicantList.add(Applicant("Wong", 10, imageId[0]))
        ApplicantList.add(Applicant("Zhi", 20, imageId[0]))
        ApplicantList.add(Applicant("Hern", 20, imageId[0]))

        //newRecyclerView.adapter = VacancyDetailAdapter(ApplicantList)


    }

}