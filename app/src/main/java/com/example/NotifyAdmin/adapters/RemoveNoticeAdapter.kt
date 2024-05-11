package com.ankur.admin_notifycampus.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ankur.admin_notifycampus.Models.NoticeModel

import com.bumptech.glide.Glide
import com.example.notify.R
import com.example.notify.databinding.NoticeUiForDeleteBinding

class RemoveNoticeAdapter(private val context: Context, val list:ArrayList<NoticeModel>, private val collectionName: String)
    :RecyclerView.Adapter<RemoveNoticeAdapter.RemoveNoticeViewHolder>() {
    inner class RemoveNoticeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val binding = NoticeUiForDeleteBinding.bind(itemView)
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: NoticeModel, position: Int) {
        list.add(position, item)
        notifyItemInserted(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemoveNoticeViewHolder {
        return RemoveNoticeViewHolder(LayoutInflater.from(context).inflate(R.layout.notice_ui_for_delete,parent,false))
    }

    override fun getItemCount(): Int {
        Log.d("adapter23",list.size.toString())
        return list.size
    }

    override fun onBindViewHolder(holder: RemoveNoticeViewHolder, position: Int) {
        var currentItem = list[position]
        Log.d("adapter23",currentItem.desc.toString())

        holder.binding.Title.text=currentItem.title
        holder.binding.dateTextView.text=currentItem.date
        holder.binding.timeTextView.text=currentItem.time

        // Inside your RecyclerView or ListView adapter's onBindViewHolder method
        val maxLength = 100 // Max length of text to display initially
        val description = currentItem.desc // Get the description text from your data model

        if (description != null) {
            if (description.length > maxLength) {
                holder.binding.descriptionTextView.text = "${description.substring(0, maxLength)}..." // Truncate text
                holder.binding.moreButton.visibility = View.VISIBLE // Show the "more" button
                holder.binding.moreButton.setOnClickListener {
                    // When the "more" button is clicked, expand the TextView to show full text
                    holder.binding.descriptionTextView.text = description
                    holder.binding.moreButton.visibility = View.GONE // Hide the "more" button
                    holder.binding.descriptionTextView.maxLines = Integer.MAX_VALUE // Show all lines
                }
                holder.binding.descriptionTextView.maxLines = 2 // Limit to 2 lines initially
            } else {
                holder.binding.descriptionTextView.text = description // Display full text
                holder.binding.moreButton.visibility = View.GONE // Hide the "more" button
            }
        } else {
            // Handle case where description is null
            holder.binding.descriptionTextView.text = "" // Clear text view if description is null
        }

        Glide.with(context).load(currentItem.imgUrl).into(holder.binding.image)

    }
}