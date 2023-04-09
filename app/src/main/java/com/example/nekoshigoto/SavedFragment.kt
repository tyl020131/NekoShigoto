package com.example.nekoshigoto

import JobAdapter
import SavedAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SavedFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var jobList : ArrayList<Job>
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

        jobList = arrayListOf<Job>()
        loadData()

        return view;


    }

    private fun loadData(){
        jobList.add(Job(imageId[0],"Kunkun Company","Product Designer","Penang, Malaysia","Full-Time"))
        jobList.add(Job(imageId[0],"Paopao Company","Tyre Mechanic","Johor, Malaysia","Freelance"))
        jobList.add(Job(imageId[0],"Ikun Company","Chef","Penang, Malaysia","Full-Time"))



        newRecyclerView.adapter = SavedAdapter(jobList)
    }


}