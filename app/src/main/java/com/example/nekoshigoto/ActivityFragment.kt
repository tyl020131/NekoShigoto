package com.example.nekoshigoto

import ApplicationAdapter
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ActivityFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var jobList : ArrayList<Application>
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

        jobList = arrayListOf<Application>()
        loadData()

        val filterBtn : ImageButton = view.findViewById(R.id.filter_home)

        filterBtn.setOnClickListener {
            FilterJobDialog(requireContext())
        }

        return view;


    }

    private fun loadData(){
        jobList.add(Application(imageId[0],"Kunkun Company","Product Designer","Penang, Malaysia","Full-Time","Rejected"))
        jobList.add(Application(imageId[0],"Paopao Company","Tyre Mechanic","Johor, Malaysia","Freelance", "Approved"))



        newRecyclerView.adapter = ApplicationAdapter(jobList)
    }
    override fun onResume() {
        super.onResume()
        val activity = activity as Home
        activity?.showBottomNav()
    }

}