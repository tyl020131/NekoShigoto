package com.example.nekoshigoto

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nekoshigoto.databinding.FragmentQualificationBinding

class QualificationFragment : Fragment() {

    private lateinit var binding : FragmentQualificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentQualificationBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val education= resources.getStringArray(R.array.education_array)

        val spinner = binding.educationLevelSpinner

        val spinnerAdapter= object : ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item, education) {

            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if(position == 0) {
                    view.setTextColor(Color.GRAY)
                } else {
                    //here it is possible to define color for other items by
                    //view.setTextColor(Color.RED)
                }
                return view
            }

        }

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if(value == education[0]){
                    (view as TextView).setTextColor(Color.GRAY)
                }
            }

        }

        binding.button2.setOnClickListener {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()?.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit)
                ?.replace(R.id.container, ProfileFragment())
                ?.commit()

        }
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.container, ProfileFragment())
                fragmentTransaction?.addToBackStack(null)
                fragmentTransaction?.commit()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> {
//                activity
//                    ?.supportFragmentManager
//                    ?.beginTransaction()?.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit)
//                    ?.replace(R.id.container, ProfileFragment())
//                    ?.commit()
//                return true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }


}