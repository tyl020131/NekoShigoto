package com.example.nekoshigoto

import FieldAdapter
import ModeAdapter
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider

@SuppressLint("ResourceAsColor")
class VacancyFilterDialog (context : Context) {
    private lateinit var dialogRecyclerView: RecyclerView
    private lateinit var modeList : ArrayList<String>
    private lateinit var fieldRecyclerView: RecyclerView
    private lateinit var fieldList : ArrayList<String>
    lateinit var dialog : Dialog
    //Adapter
    private lateinit var fieldAdapter: FieldAdapter
    private lateinit var modeAdapter: ModeAdapter
    //Filter Variables
    var gender = "All"
    var status = "All"
    var sort: String = "Salary High"
    var salary_range : ArrayList<Int> = ArrayList<Int>()
    lateinit var fields : ArrayList<String>
    lateinit var modes : ArrayList<String>
    init{
        //salary range
        salary_range.add(0)
        salary_range.add(100000)
        //setup
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.vacancy_filter_dialog)
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

        //Events
        val btn : ImageButton = dialog.findViewById(R.id.back_btn)
        val salary_val: TextView = dialog.findViewById(R.id.salary_val)
        val continuousRangeSlider: RangeSlider = dialog.findViewById(com.example.nekoshigoto.R.id.continuousRangeSlider)
        continuousRangeSlider.setValues(0f, 100000f)
        continuousRangeSlider.setLabelFormatter(object : LabelFormatter {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        })

        continuousRangeSlider.addOnChangeListener { slider, value, fromUser ->
            salary_val.text = "RM${slider.values[0].toInt()} - RM${slider.values[1].toInt()}"
            salary_range[0] = slider.values[0].toInt()
            salary_range[1] = slider.values[1].toInt()
        }
        btn.setOnClickListener {
            dialog.dismiss()
        }

        //Sorter
        val shigh = dialog.findViewById<TextView>(R.id.salary_high)
        val slow = dialog.findViewById<TextView>(R.id.salary_low)

        val salary_high = dialog.findViewById<TextView>(R.id.salhigh_tick)
        val salary_low = dialog.findViewById<TextView>(R.id.sallow_tick)

        shigh.setOnClickListener {
            salary_high.visibility = View.VISIBLE
            salary_low.visibility = View.INVISIBLE
            sort="Salary High"
        }

        slow.setOnClickListener {
            salary_low.visibility = View.VISIBLE
            salary_high.visibility = View.INVISIBLE
            sort="Salary Low"
        }

        val radioGrp : RadioGroup = dialog.findViewById<RadioGroup>(R.id.grp_gender)
        radioGrp.setOnCheckedChangeListener { group, checkedId ->
            val selected = group.findViewById<RadioButton>(group.checkedRadioButtonId)
            gender = selected.text.toString()
        }

        val statusGrp : RadioGroup = dialog.findViewById<RadioGroup>(R.id.grp_status)
        statusGrp.setOnCheckedChangeListener { group, checkedId ->
            val selected = group.findViewById<RadioButton>(group.checkedRadioButtonId)
            status = selected.text.toString()
        }

        dialog.show()
    }

    fun updateField(){
        fields = fieldAdapter.getSFields()
        modes = modeAdapter.getSModes()
    }
    private fun loadField(){
        fieldList.add("Agriculture")
        fieldList.add("Architecture")
        fieldList.add("Arts and Entertainment")
        fieldList.add("Business and Finance")
        fieldList.add("Social Community")
        fieldList.add("Computer and IT")
        fieldList.add("Mathematics")
        fieldList.add("Education")
        fieldList.add("Healthcare")
        fieldList.add("Maintenance and Repair")
        fieldList.add("Law")
        fieldList.add("Management")
        fieldList.add("Communication")
        fieldList.add("Office")
        fieldList.add("Personal Care and Service")
        fieldList.add("Production")
        fieldList.add("Sales")
        fieldList.add("Transportation")
        fieldList.add("Food")
        fieldList.add("Others")

        fieldAdapter = FieldAdapter(fieldList)
        fieldRecyclerView.adapter = fieldAdapter
    }

    private fun loadMode(){
        modeList.add("Freelance")
        modeList.add("Part-Time")
        modeList.add("Full-Time")

        modeAdapter = ModeAdapter(modeList)
        dialogRecyclerView.adapter = modeAdapter
    }
}