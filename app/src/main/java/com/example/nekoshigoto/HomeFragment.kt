package com.example.nekoshigoto

import FieldAdapter
import JobAdapter
import ModeAdapter
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


class HomeFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var jobList : ArrayList<JobViewModel>
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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        imageId = arrayOf(
            R.drawable.kunkun
        )
        newRecyclerView = view.findViewById(R.id.jobs)
        newRecyclerView.layoutManager = LinearLayoutManager(activity);
        newRecyclerView.setHasFixedSize(true)

        jobList = arrayListOf<JobViewModel>()
        loadData()

        val filterBtn : ImageButton = view.findViewById(R.id.filter_home)

        filterBtn.setOnClickListener {
            showDialog()
        }


        return view;


    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.customer_filter_dialog)

        fieldRecyclerView = dialog.findViewById(R.id.fieldRecycler)
        dialogRecyclerView = dialog.findViewById(R.id.modeRecycler)
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

        val btn : ImageButton = dialog.findViewById(R.id.back_btn)
        val salary_val: TextView = dialog.findViewById(R.id.salary_val)
        val continuousRangeSlider: RangeSlider = dialog.findViewById(R.id.continuousRangeSlider)
        continuousRangeSlider.addOnChangeListener { slider, value, fromUser ->
            salary_val.text = "RM${String.format("%.2f",slider.values[0])} - RM${String.format("%.2f",slider.values[1])}"
        }
        btn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

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
    private fun loadData(){
        jobList.add(JobViewModel(imageId[0],"Kunkun Company","Product Designer","Penang, Malaysia","Full-Time"))
        jobList.add(JobViewModel(imageId[0],"Paopao Company","Tyre Mechanic","Johor, Malaysia","Freelance"))



        newRecyclerView.adapter = JobAdapter(jobList)
    }


}