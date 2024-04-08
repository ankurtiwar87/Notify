package com.example.notify.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notify.R
import com.example.notify.databinding.FacultyUiBinding
import com.example.notify.models.FacultyModel

class FacultyAdapter(private val context: Context,private val list:List<FacultyModel>):RecyclerView.Adapter<FacultyAdapter.FacultyViewHolder>(){

    inner class FacultyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val binding=FacultyUiBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacultyViewHolder {
        return FacultyViewHolder(LayoutInflater.from(context).inflate(R.layout.faculty_ui,parent,false))
    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: FacultyViewHolder, position: Int) {
        val currentItem =list[position]

        Glide.with(context).load(currentItem.imageUrl).into(holder.binding.FacultyImage)
        holder.binding.name.text=currentItem.name
        holder.binding.email.text=currentItem.email
        holder.binding.phone.text=currentItem.phoneNo
        holder.binding.Cabin.text=currentItem.cabinNo
        holder.binding.block.text=currentItem.block
        holder.binding.Year.text=currentItem.year
        holder.binding.subject.text=currentItem.subject

    }
}