package com.example.nekoshigoto

import JobAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Home : AppCompatActivity() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var jobList : ArrayList<JobViewModel>
    lateinit var imageId : Array<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        imageId = arrayOf(
            R.drawable.kunkun
        )
        newRecyclerView = findViewById(R.id.jobs)
        newRecyclerView.layoutManager = LinearLayoutManager(this);
        newRecyclerView.setHasFixedSize(true)

        jobList = arrayListOf<JobViewModel>()
        loadData()

    }

    private fun loadData(){
        jobList.add(JobViewModel(imageId[0],"Kunkun Company","Product Designer","Penang, Malaysia","Full-Time"))
        jobList.add(JobViewModel(imageId[0],"Paopao Company","Tyre Mechanic","Johor, Malaysia","Freelance"))



        newRecyclerView.adapter = JobAdapter(jobList)
    }
}