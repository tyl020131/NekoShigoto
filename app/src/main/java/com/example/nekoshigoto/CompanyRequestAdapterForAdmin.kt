package com.example.nekoshigoto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompanyRequestAdapterForAdmin(private val companyRequestListForAdmin : ArrayList<CompanyRequest>) :
    RecyclerView.Adapter<CompanyRequestAdapterForAdmin.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompanyRequestAdapterForAdmin.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.company_request_list,parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return companyRequestListForAdmin.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = companyRequestListForAdmin[position]
        holder.name.text = currentItem.name
        holder.status.text = currentItem.status
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var name : TextView = itemView.findViewById(R.id.company_name)
        var status : TextView = itemView.findViewById(R.id.company_status)

    }
}