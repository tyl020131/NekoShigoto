package com.example.nekoshigoto

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.nekoshigoto.databinding.FragmentQualificationBinding
import com.github.dhaval2404.imagepicker.ImagePicker

class QualificationFragment : Fragment() {

    private lateinit var binding : FragmentQualificationBinding
    private lateinit var pdfUri: Uri
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentQualificationBinding.inflate(inflater, container, false)

        //education spinner setup
        val education= resources.getStringArray(R.array.education_array)
        val eduSpinner = binding.educationLevelSpinner
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
                    view.setTextColor(resources.getColor(R.color.color1))
                }
                return view
            }

        }


        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        eduSpinner.adapter = spinnerAdapter

        eduSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
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

        var fieldArray =  resources.getStringArray(R.array.field_array)
        fieldArray = arrayOf("Select Select Your Field...") + fieldArray
        val fieldSpinner = binding.fieldSpinner
        val spinnerAdapter2= object : ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item, fieldArray) {

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
                    view.setTextColor(resources.getColor(R.color.color1))
                }
                return view
            }

        }


        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fieldSpinner.adapter = spinnerAdapter2

        fieldSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if(value == fieldArray[0]){
                    (view as TextView).setTextColor(Color.GRAY)
                }
            }

        }


        binding.button2.setOnClickListener {

        }

        binding.editTextResume.setOnClickListener {
            selectPdf()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as Home?
        activity?.hideBottomNav()
    }

    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, 12)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // For loading PDF
        when (requestCode) {
            12 -> if (resultCode == AppCompatActivity.RESULT_OK) {

                pdfUri = data?.data!!
                val uri: Uri = data?.data!!
                val uriString: String = uri.toString()
                var pdfName: String? = null
                if (uriString.startsWith("content://")) {
                    var myCursor: Cursor? = null
                    try {
                        myCursor = requireContext().applicationContext!!.contentResolver.query(uri, null, null, null, null)
                        if (myCursor != null && myCursor.moveToFirst()) {
                            val nameIndex = myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            if (nameIndex >= 0) {
                                pdfName = myCursor.getString(nameIndex)
                            }
                            binding.editTextResume.text = pdfName
                        }
                    } finally {
                        myCursor?.close()
                    }
                }
            }
        }
    }


}