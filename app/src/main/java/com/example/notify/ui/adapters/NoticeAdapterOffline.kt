package com.example.notify.ui.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notify.R
import com.example.notify.Room.NoticeEntity
import com.example.notify.databinding.NoticeUiBinding
import com.example.notify.models.NoticeModel
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class NoticeAdapterOffline(private val context: Context, val list: List<NoticeEntity>)
    :RecyclerView.Adapter<NoticeAdapterOffline.RemoveNoticeViewHolder>() {
    inner class RemoveNoticeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val binding =NoticeUiBinding.bind(itemView)
    }

//    fun removeItem(position: Int) {
//        list.removeAt(position)
//        notifyItemRemoved(position)
//    }
//
//    fun restoreItem(item: NoticeModel, position: Int) {
//        list.add(position, item)
//        notifyItemInserted(position)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemoveNoticeViewHolder {
        return RemoveNoticeViewHolder(LayoutInflater.from(context).inflate(R.layout.notice_ui,parent,false))
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

        holder.binding.downloadButton.setOnClickListener {
            val downloadUrl = currentItem.docUrl

            // Check if the download URL is present
            if (downloadUrl != null && downloadUrl.isNotEmpty()) {
                // Download the document
                downloadDocument(downloadUrl)
            } else {
                // Display a toast message indicating that no document is present
                Toast.makeText(context, "No document available", Toast.LENGTH_SHORT).show()
            }
        }

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

    private fun downloadDocument(downloadUrl: String) {
        // Create a FirebaseStorage instance
        val storage = FirebaseStorage.getInstance()

        // Get reference to the document in FirebaseStorage
        val storageRef = storage.getReferenceFromUrl(downloadUrl)

        // Create a local file to save the document
        val localFile = File.createTempFile("document", "pdf")

        // Download the document to the local file
        storageRef.getFile(localFile)
            .addOnSuccessListener {
                // Document downloaded successfully
                // Here, you can open the downloaded document using an appropriate application
                // For example, you can use Intent to open a PDF viewer
                val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", localFile)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, "application/pdf")
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.startActivity(intent)
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occur during the download
                Log.e("ViewNoticeFragment", "Error downloading document: $exception")
                // Display a toast message indicating the error
                Toast.makeText(context, "Error downloading document", Toast.LENGTH_SHORT).show()
            }
    }
}