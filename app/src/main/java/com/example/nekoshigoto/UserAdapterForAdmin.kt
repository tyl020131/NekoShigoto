package com.example.nekoshigoto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nekoshigoto.R
import com.example.nekoshigoto.User


class UserAdapterForAdmin(private val userListForAdmin : ArrayList<UserView>) :
    RecyclerView.Adapter<UserAdapterForAdmin.MyViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserAdapterForAdmin.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_view_list,parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userListForAdmin.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userListForAdmin[position]
        holder.name.text = currentItem.name
        holder.status.text = currentItem.status
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var name : TextView = itemView.findViewById(R.id.user_name)
        var status : TextView = itemView.findViewById(R.id.user_status)
    }
}