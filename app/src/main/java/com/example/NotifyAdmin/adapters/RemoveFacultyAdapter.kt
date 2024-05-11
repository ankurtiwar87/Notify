package com.ankur.admin_notifycampus.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ankur.admin_notifycampus.Models.FacultyModel
import com.bumptech.glide.Glide
import com.example.notify.R
import com.example.notify.databinding.FacultyUiForDeleteBinding

class RemoveFacultyAdapter(private val context: Context, val list :ArrayList<FacultyModel>,private val collectionName: String):RecyclerView.Adapter<RemoveFacultyAdapter.RemoveFacultyViewHolder>() {
   inner class RemoveFacultyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
       val binding= FacultyUiForDeleteBinding.bind(itemView)

    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: FacultyModel, position: Int) {
        list.add(position, item)
        notifyItemInserted(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemoveFacultyViewHolder {
        return RemoveFacultyViewHolder(LayoutInflater.from(context).inflate(R.layout.faculty_ui_for_delete,parent,false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: RemoveFacultyViewHolder, position: Int) {
        val  currentItem =list[position]

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