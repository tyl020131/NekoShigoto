package com.example.nekoshigoto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class CompanyAdapterForAdmin(private val companyListForAdmin : ArrayList<CompanyView>) :
        RecyclerView.Adapter<CompanyAdapterForAdmin.MyViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompanyAdapterForAdmin.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.company_view_list,parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return companyListForAdmin.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = companyListForAdmin[position]
        holder.numbering.text = (position + 1).toString()
        holder.name.text = currentItem.name
        holder.status.text = currentItem.status

        holder.viewDetailBtn.tag = currentItem.email
        holder.viewDetailBtn.setOnClickListener { view->
            /*val tag = view.tag as String
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(record, tag)
            findNavController().navigate(action)*/

            //val bundle = bundleOf("dataKey" to "data")
            view.findNavController().navigate(R.id.action_adminViewCompanyFragment2_to_adminCompanyDetailViewFragment)
        }
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var numbering : TextView = itemView.findViewById(R.id.numbering)
        var name : TextView = itemView.findViewById(R.id.company_name)
        var status : TextView = itemView.findViewById(R.id.company_status)
        var viewDetailBtn : Button = itemView.findViewById<Button>(R.id.viewCompanyDetailBtn)
    }
}

