package com.example.nekoshigoto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.example.nekoshigoto.Applicant
import com.example.nekoshigoto.R
import androidx.recyclerview.widget.RecyclerView
import coil.load
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class VacancyDetailAdapter(private val applicantList : ArrayList<JobSeeker>) :
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
        holder.applicantAge.text = age.toString()

        val imgUri = currentItem.profilePic.toUri().buildUpon().scheme("https").build()
        holder.applicantImage.load(imgUri)
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var applicantImage: ImageView
        var applicantName: TextView
        var applicantAge: TextView

        init{
            applicantImage = itemView.findViewById(R.id.applicantImage)
            applicantName = itemView.findViewById(R.id.applicantName)
            applicantAge = itemView.findViewById(R.id.applicantAge)
        }
    }
}