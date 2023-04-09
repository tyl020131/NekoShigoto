package com.example.nekoshigoto

import FieldAdapter
import ModeAdapter
import VacancyAdapter
import android.app.Dialog
import android.os.Bundle
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


class VacancyFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var VacancyList : ArrayList<Job>
    lateinit var imageId : Array<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_vacancy, container, false)

        imageId = arrayOf(
            R.drawable.kunkun
        )
        newRecyclerView = view.findViewById(R.id.vacancies)
        newRecyclerView.layoutManager = LinearLayoutManager(activity);
        newRecyclerView.setHasFixedSize(true)

        VacancyList = arrayListOf<Job>()
        loadData()

        val filterBtn : ImageButton = view.findViewById(R.id.filter_home)

        filterBtn.setOnClickListener {
            //showDialog()
        }


        return view;


    }


    private fun loadData(){
        VacancyList.add(Job(imageId[0],"Kunkun Company","Product Designer","Penang, Malaysia","Full-Time"))
        VacancyList.add(Job(imageId[0],"Paopao Company","Tyre Mechanic","Johor, Malaysia","Freelance"))



        newRecyclerView.adapter = VacancyAdapter(VacancyList)
    }


}