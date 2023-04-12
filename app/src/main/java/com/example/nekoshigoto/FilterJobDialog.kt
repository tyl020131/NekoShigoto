package com.example.nekoshigoto

import FieldAdapter
import JobAdapter
import ModeAdapter
import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.slider.RangeSlider

class FilterJobDialog(context : Context) {
    private lateinit var dialogRecyclerView: RecyclerView
    private lateinit var modeList : ArrayList<String>
    private lateinit var fieldRecyclerView: RecyclerView
    private lateinit var fieldList : ArrayList<String>

    init{
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.customer_filter_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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
    private fun loadField(){
        fieldList.add("IT")
        fieldList.add("Software Engineering")
        fieldList.add("Finance")
        fieldList.add("noob")
        fieldList.add("League Of Legends")

        fieldRecyclerView.adapter = FieldAdapter(fieldList)
    }

    private fun loadMode(){
        modeList.add("Freelance")
        modeList.add("Part-Time")
        modeList.add("Full-Time")

        dialogRecyclerView.adapter = ModeAdapter(modeList)
    }
}