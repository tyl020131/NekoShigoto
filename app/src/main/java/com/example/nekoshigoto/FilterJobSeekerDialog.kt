
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
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
class FilterJobSeekerDialog(context : Context) {
    private lateinit var dialogRecyclerView: RecyclerView
    private lateinit var modeList : ArrayList<String>
    lateinit var dialog : Dialog
    //Adapter
    private lateinit var modeAdapter: ModeAdapter
    //Filter Variables
    var gender = "Male"
    var nationality = "Malaysian"
    var age_range : ArrayList<Int> = ArrayList<Int>()
    lateinit var modes : ArrayList<String>
    init{
        //salary range
        age_range.add(15)
        age_range.add(70)
        //setup
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(com.example.nekoshigoto.R.layout.jobseeker_filter_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogRecyclerView = dialog.findViewById(com.example.nekoshigoto.R.id.modeRecycler)
        val layoutManager2 : FlexboxLayoutManager = FlexboxLayoutManager(context)
        layoutManager2.setFlexWrap(FlexWrap.WRAP);
        dialogRecyclerView.layoutManager = layoutManager2
        modeList = arrayListOf<String>()
        loadMode()

        //Events
        val btn : ImageButton = dialog.findViewById(com.example.nekoshigoto.R.id.back_btn)
        val age_val: TextView = dialog.findViewById(com.example.nekoshigoto.R.id.age_val)
        val continuousRangeSlider: RangeSlider = dialog.findViewById(com.example.nekoshigoto.R.id.continuousRangeSlider)
        continuousRangeSlider.setValues(15f, 100f)
        continuousRangeSlider.setLabelFormatter(object : LabelFormatter {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        })

        continuousRangeSlider.addOnChangeListener { slider, value, fromUser ->
            age_val.text = "${slider.values[0].toInt()} - ${slider.values[1].toInt()}"
            age_range[0] = slider.values[0].toInt()
            age_range[1] = slider.values[1].toInt()
        }

        btn.setOnClickListener {
            dialog.dismiss()
        }

        val radioGrp : RadioGroup = dialog.findViewById<RadioGroup>(com.example.nekoshigoto.R.id.grp_gender)
        radioGrp.setOnCheckedChangeListener { group, checkedId ->
            val selected = group.findViewById<RadioButton>(group.checkedRadioButtonId)
            gender = selected.text.toString()
        }

        val nationalityGrp : RadioGroup = dialog.findViewById<RadioGroup>(com.example.nekoshigoto.R.id.grp_nationality)
        nationalityGrp.setOnCheckedChangeListener { group, checkedId ->
            val selected = group.findViewById<RadioButton>(group.checkedRadioButtonId)
            nationality = selected.text.toString()
        }

        dialog.show()
    }

    fun updateField(){
        modes = modeAdapter.getSModes()
    }

    private fun loadMode(){
        modeList.add("Freelance")
        modeList.add("Part-Time")
        modeList.add("Full-Time")

        modeAdapter = ModeAdapter(modeList)
        dialogRecyclerView.adapter = modeAdapter
    }
}