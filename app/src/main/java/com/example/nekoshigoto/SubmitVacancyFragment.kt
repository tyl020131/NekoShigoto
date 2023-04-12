package com.example.nekoshigoto

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.Slider
import com.google.firebase.firestore.FirebaseFirestore


class SubmitVacancyFragment : Fragment() {

    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_submit_vacancy, container, false)



        val salary_val: TextView = view.findViewById(R.id.salary_val)
        val normalContinuousSlider: Slider = view.findViewById(R.id.normalContinuousSlider)
        normalContinuousSlider.addOnChangeListener { slider, value, fromUser ->
            salary_val.text = "RM${String.format("%.2f",value)}}"
        }

        val submit_button:Button = view.findViewById(R.id.submit_vacancy_button)
        submit_button.setOnClickListener {
            val gender = view.findViewById<RadioGroup>(R.id.my_gender)
            val position = view.findViewById<EditText>(R.id.enter_position)
            val field = view.findViewById<EditText>(R.id.enter_field)
            val mode = view.findViewById<RadioGroup>(R.id.my_mode)
            val description = view.findViewById<EditText>(R.id.desc_val)
            //values
            val position_val = position.text.toString();
            val field_val = field.text.toString();
            val mode_val = mode.findViewById<RadioButton>(mode.checkedRadioButtonId).text.toString()
            val gender_val = gender.findViewById<RadioButton>(gender.checkedRadioButtonId).text.toString()
            val salary_val = normalContinuousSlider.value.toString()
            val description_val = description.text.toString()

            var sh : SharedPreferences = requireActivity().getSharedPreferences("SessionSharedPref", Context.MODE_PRIVATE)

            val companyName : String = sh.getString("CompanyEmail","").toString()

            val vacancy = Vacancy(position_val,field_val,mode_val,gender_val,salary_val.toDouble(),description_val,companyName)

            val collectionid = String.format("%s%s",position_val,companyName).lowercase()
            db.collection("Vacancy").document(collectionid).set(vacancy).addOnFailureListener {
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
            }

            findNavController().navigate(R.id.action_submitVacancyFragment_to_vacancyFragment)





        }




        return view;


    }





}