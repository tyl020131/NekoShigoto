package com.example.nekoshigoto

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.nekoshigoto.databinding.FragmentSubmitVacancyBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.slider.Slider
import com.google.firebase.firestore.FirebaseFirestore


class SubmitVacancyFragment : Fragment() {
    private lateinit var view: FragmentSubmitVacancyBinding
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        // Inflate the layout for this fragment
        view = FragmentSubmitVacancyBinding.inflate(layoutInflater)

        val viewModel: CompanyViewModel = ViewModelProvider(requireActivity()).get(CompanyViewModel::class.java)

        view.vacancyCompanyName.text = viewModel.getCompany().name
        val imgUrl = viewModel.getCompany().profilePic.toUri().buildUpon().scheme("https").build()
        view.vacancyCompanyPropic.load(imgUrl)
        //val fields = resources.getStringArray(R.array.Fields)
        val spinner:Spinner = view.fieldspinner
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.Fields,

                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
        }



        val salary_val: TextView = view.salaryVal
        val normalContinuousSlider: Slider = view.normalContinuousSlider
        normalContinuousSlider.addOnChangeListener { slider, value, fromUser ->
            salary_val.text = "RM${String.format("%d",value)}"
        }

        val submit_button:Button = view.submitVacancyButton
        submit_button.setOnClickListener {
           val gender = view.myGender
            val position = view.enterPosition
            val spinner = view.fieldspinner
            val mode = view.myMode
            val description = view.descVal
            var spinner_val = spinner.selectedItem.toString();
            //values
            val position_val = position.text.toString();
            val field_val = spinner_val.toString();
            val mode_val = mode.findViewById<RadioButton>(mode.checkedRadioButtonId).text.toString()
            val gender_val = gender.findViewById<RadioButton>(gender.checkedRadioButtonId).text.toString()
            val salary_val = normalContinuousSlider.value.toString()
            val description_val = description.text.toString()

            if(position_val.isEmpty()){
                Toast.makeText(getActivity(), "Please Enter Position Name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(normalContinuousSlider.value.equals(0.00)){
                Toast.makeText(getActivity(), "Please Enter Salary for your vacancy!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(description_val.isEmpty()){
                Toast.makeText(getActivity(), "Please Some Description for your vacancy!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }




            val companyName = viewModel.getCompany().name

            val collectionid = String.format("%s%s",position_val,companyName).lowercase()
            val company = viewModel.getCompany()
            val vacancy = Vacancy(collectionid,viewModel.getCompany().profilePic,position_val,field_val,mode_val,gender_val,salary_val.toDouble(),description_val,companyName,String.format("%s,%s",company.state,company.country))


            db.collection("Vacancy").document(collectionid).set(vacancy).addOnFailureListener {
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
            }



            findNavController().navigate(R.id.action_submitVacancyFragment_to_vacancyFragment)

            Toast.makeText(getActivity(), "Vacancy Created Successfully!", Toast.LENGTH_SHORT).show()





        }




        return view.root;


    }





}