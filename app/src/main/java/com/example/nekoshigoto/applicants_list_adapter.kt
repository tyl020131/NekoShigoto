package com.example.nekoshigoto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class applicants_list_adapter: RecyclerView.Adapter<applicants_list_adapter.ViewHolder>() {

    private var applicantNameArr = arrayOf("Wong", "Zhi", "Hern","Wong", "Zhi", "Hern","Wong", "Zhi", "Hern")
    private var applicantAgeArr = arrayOf("10", "20", "30","10", "20", "30","10", "20", "30")
    private var applicantImageArr = intArrayOf(R.drawable.megumin,R.drawable.megumin,R.drawable.megumin,R.drawable.megumin,R.drawable.megumin,R.drawable.megumin,R.drawable.megumin,R.drawable.megumin,R.drawable.megumin)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): applicants_list_adapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.applicants_list, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: applicants_list_adapter.ViewHolder, position: Int) {
        holder.applicantName.text = applicantNameArr[position]
        holder.applicantAge.text = applicantAgeArr[position]
        holder.applicantImage.setImageResource(applicantImageArr[position])
    }

    override fun getItemCount(): Int {
        return applicantNameArr.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
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