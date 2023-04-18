package com.example.nekoshigoto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.nekoshigoto.Applicant
import com.example.nekoshigoto.R
import androidx.recyclerview.widget.RecyclerView

class VacancyDetailAdapter(private val applicantList : ArrayList<Applicant>) :
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
        holder.applicantName.text = currentItem.name
        holder.applicantAge.text = currentItem.age.toString()
        holder.applicantImage.setImageResource(currentItem.image)
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