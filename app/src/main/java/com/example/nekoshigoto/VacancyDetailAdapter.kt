package com.example.nekoshigoto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.example.nekoshigoto.Applicant
import com.example.nekoshigoto.R
import androidx.recyclerview.widget.RecyclerView
import coil.load
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class VacancyDetailAdapter(private val applicantList : ArrayList<JobSeeker>,private val vacID:String) :
    RecyclerView.Adapter<VacancyDetailAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.applicants_list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return applicantList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = applicantList[position];
        holder.applicantName.text = currentItem.fname + " " + currentItem.lname

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val dob = LocalDate.parse(currentItem.dob, formatter)
        val currentDate: LocalDate = LocalDate.now()
        val age: Int = Period.between(dob, currentDate).getYears()
        holder.applicantAge.text = age.toString() + " Years Old"

        val imgUri = currentItem.profilePic.toUri().buildUpon().scheme("https").build()
        holder.applicantImage.load(imgUri)

        holder.viewButton.setOnClickListener {
            val bundle = bundleOf("email" to currentItem.email, "vacID" to vacID)
            it.findNavController().navigate(R.id.action_vacancyDetailFragment_to_companyApplicationDetailFragment, bundle)
        }

    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var applicantImage: ImageView
        var applicantName: TextView
        var applicantAge: TextView
        var viewButton : Button

        init{
            applicantImage = itemView.findViewById(R.id.applicantImage)
            applicantName = itemView.findViewById(R.id.applicantName)
            applicantAge = itemView.findViewById(R.id.applicantAge)
            viewButton = itemView.findViewById(R.id.viewButton)
        }
    }
}