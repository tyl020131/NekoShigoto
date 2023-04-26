package com.example.nekoshigoto

import FieldAdapter
import ModeAdapter
import VacancyAdapter
import android.app.Dialog
import android.content.Intent
import android.content.ContentValues.TAG
import android.os.Bundle
import android.view.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject


class VacancyFragment : Fragment() {
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var VacancyList : ArrayList<Vacancy>
    lateinit var imageId : Array<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        setHasOptionsMenu(true)

        val viewModel: CompanyViewModel = ViewModelProvider(requireActivity()).get(CompanyViewModel::class.java)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_vacancy, container, false)

        imageId = arrayOf(
            R.drawable.kunkun
        )
        newRecyclerView = view.findViewById(R.id.vacancies)
        newRecyclerView.layoutManager = LinearLayoutManager(activity);
        newRecyclerView.setHasFixedSize(true)

        VacancyList = arrayListOf<Vacancy>()
        loadData(viewModel.getCompany().name)

        val filterBtn : ImageButton = view.findViewById(R.id.filter_home)

        filterBtn.setOnClickListener {
            //showDialog()
        }

        val create_button : Button = view.findViewById(R.id.create_vacancy_btn)
        create_button.setOnClickListener {
            this.findNavController().navigate(R.id.action_vacancyFragment_to_submitVacancyFragment)
        }


        return view;


    }


    private fun loadData(name:String) {


        db.collection("Vacancy")
            .whereEqualTo("companyName", name)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val vacancy = document.toObject(Vacancy::class.java)
                    if (document != null) {

                        val query = db.collection("Vacancy").document("${document.id}").collection("application")
                        query.get().addOnSuccessListener {
                            vacancy.numOfApp = it.documents.size
                        }


                    }
                    VacancyList.add(vacancy)


                }
                newRecyclerView.adapter = VacancyAdapter(VacancyList)

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }


        }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
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
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        val activity = activity as CompanyHome
        activity?.showBottomNav()
    }

}