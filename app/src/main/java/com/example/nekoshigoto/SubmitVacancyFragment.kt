package com.example.nekoshigoto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import coil.load
import com.example.nekoshigoto.databinding.FragmentSubmitVacancyBinding
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.Slider
import com.google.firebase.firestore.FirebaseFirestore


class SubmitVacancyFragment : Fragment() {
    private lateinit var view: FragmentSubmitVacancyBinding
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        setHasOptionsMenu(true)
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
            salary_val.text = "RM${String.format("%d", value.toInt())}"
        }

        normalContinuousSlider.setLabelFormatter(object : LabelFormatter {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        })



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
            val salary = salary_val.toFloat().toInt()
            val description_val = description.text.toString()

            if(position_val.isEmpty()){
                Toast.makeText(getActivity(), "Please Enter Position Name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if(normalContinuousSlider.value<=0){
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
            val vacancy = Vacancy(collectionid,viewModel.getCompany().profilePic,position_val,field_val,mode_val,gender_val,salary,description_val,companyName,"Active", String.format("%s,%s",company.state,company.country), 0)

            db.collection("Vacancy").document(collectionid).get().addOnSuccessListener { snapshot ->
                if (snapshot != null && snapshot.exists() && snapshot.getString("status")!="Inactive") {
                    // Document exists, show toast message
                    Toast.makeText(requireActivity(), "Vacancy already exists", Toast.LENGTH_SHORT).show()
                } else if (snapshot != null && snapshot.exists() && snapshot.getString("status") == "Inactive"){
                    db.collection("Vacancy").document(collectionid).collection("Application").get().addOnSuccessListener{documents ->
                        for (document in documents) {
                            db.collection("Vacancy").document(collectionid).collection("Application").document(document.id).delete()
                        }
                        createNewVacancy(collectionid, vacancy)
                    }
                }else{
                    // Document doesn't exist, create it
                    createNewVacancy(collectionid, vacancy)
                }
            }
        }
        return view.root;
    }
    override fun onResume() {
        super.onResume()
        val activity = activity as CompanyHome
        activity?.hideBottomNav()
        activity?.chgTitle("Create New Vacancy")
    }
    fun createNewVacancy(collectionid:String, vacancy:Vacancy) {
        db.collection("Vacancy").document(collectionid).set(vacancy).addOnSuccessListener {
            findNavController().navigate(R.id.action_submitVacancyFragment_to_vacancyFragment)
            Toast.makeText(getActivity(), "Vacancy Created Successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { exception ->
            Toast.makeText(requireActivity(), exception.message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.changePasswordFragment -> {
                NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                return true
            }
            R.id.logout -> {
                startActivity(Intent(requireContext(), Logout::class.java))
            }
            R.id.companyEmailTemplateFragment -> {
                NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}