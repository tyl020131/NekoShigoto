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


class SubmitVacancyFragment : Fragment() {
    private lateinit var dialogRecyclerView: RecyclerView
    private lateinit var modeList : ArrayList<String>
    private lateinit var fieldRecyclerView: RecyclerView
    private lateinit var fieldList : ArrayList<String>
    lateinit var imageId : Array<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_submit_vacancy, container, false)

        fieldRecyclerView = view.findViewById(R.id.fieldRecycler)
        dialogRecyclerView = view.findViewById(R.id.modeRecycler)
        val layoutManager1 : FlexboxLayoutManager = FlexboxLayoutManager(context)
        layoutManager1.setFlexWrap(FlexWrap.WRAP);
        fieldRecyclerView.layoutManager = layoutManager1
        val layoutManager2 : FlexboxLayoutManager = FlexboxLayoutManager(context)
        layoutManager2.setFlexWrap(FlexWrap.WRAP);
        dialogRecyclerView.layoutManager = layoutManager2
        fieldList = arrayListOf<String>()
        modeList = arrayListOf<String>()
        loadField()
        loadMode()


        return view;


    }

    private fun loadMode(){
        modeList.add("Freelance")
        modeList.add("Part-Time")
        modeList.add("Full-Time")

        dialogRecyclerView.adapter = ModeAdapter(modeList)
    }

    private fun loadField(){
        fieldList.add("IT")
        fieldList.add("Software Engineering")
        fieldList.add("Finance")
        fieldList.add("noob")
        fieldList.add("League Of Legends")

        fieldRecyclerView.adapter = FieldAdapter(fieldList)
    }


}