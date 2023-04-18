package com.example.nekoshigoto

//import VacancyDetailAdapter
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout.TabGravity

class VacancyDetailFragment : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var ApplicantList : ArrayList<Applicant>
    lateinit var imageId : Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        imageId = arrayOf(
            R.drawable.kunkun
        )




    }



    private fun loadData(){
        ApplicantList.add(Applicant("Wong", 10, imageId[0]))
        ApplicantList.add(Applicant("Zhi", 20, imageId[0]))

        newRecyclerView.adapter = VacancyDetailAdapter(ApplicantList)


    }
}