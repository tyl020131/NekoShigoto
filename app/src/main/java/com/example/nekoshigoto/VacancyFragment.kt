package com.example.nekoshigoto

import FieldAdapter
import ModeAdapter
import VacancyAdapter
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider


class VacancyFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var VacancyList : ArrayList<Job>
    lateinit var imageId : Array<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_vacancy, container, false)

        imageId = arrayOf(
            R.drawable.kunkun
        )
        newRecyclerView = view.findViewById(R.id.vacancies)
        newRecyclerView.layoutManager = LinearLayoutManager(activity);
        newRecyclerView.setHasFixedSize(true)

        VacancyList = arrayListOf<Job>()
        loadData()

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


    private fun loadData(){
        VacancyList.add(Job(imageId[0],"Kunkun Company","Product Designer","Penang, Malaysia","Full-Time"))
        VacancyList.add(Job(imageId[0],"Paopao Company","Tyre Mechanic","Johor, Malaysia","Freelance"))



        newRecyclerView.adapter = VacancyAdapter(VacancyList)
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