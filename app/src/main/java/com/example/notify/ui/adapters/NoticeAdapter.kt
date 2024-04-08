package com.example.notify.ui.adapters

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.notify.R
import com.example.notify.databinding.NoticeUiBinding
import com.example.notify.models.NoticeModel
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class NoticeAdapter(private val context: Context, val list: List<NoticeModel>)
    :RecyclerView.Adapter<NoticeAdapter.RemoveNoticeViewHolder>() {
    inner class RemoveNoticeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val binding =NoticeUiBinding.bind(itemView)
    }


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
            Log.d("URl of Doc ","url is $downloadUrl")
            if (!downloadUrl.isNullOrEmpty()) {
                // Download the document
                downloadDocument(downloadUrl)
            } else {
                // Handle the case when docUrl is null or empty
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
        val localFile = File.createTempFile("document", "")

        // Download the document to the local file
        storageRef.getFile(localFile)
            .addOnSuccessListener {
                // Document downloaded successfully
                // Get the MIME type based on the file extension
                val mimeType = getMimeType(localFile)

                // Open the downloaded document using an appropriate application
                val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", localFile)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, mimeType)
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

  private  val DIRECTORY = Environment.DIRECTORY_DOWNLOADS

//    private fun downloadDocument(context: Context, downloadUrl: String) {
//        val uri = Uri.parse(downloadUrl)
//        val request = DownloadManager.Request(uri)
//
//        // Get the file name from the download URL
//        val fileName = getFileNameFromUrl(downloadUrl)
//        // Set the title of the download notification
//        request.setTitle("$fileName")
//
//        // Set the description of the download notification
//        request.setDescription("Downloading...")
//
//
//
//        // Set the destination directory and file name for the downloaded file
//        request.setDestinationInExternalFilesDir(context, DIRECTORY, fileName)
//
//        // Get the download service and enqueue the download request
//        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//        val downloadId = downloadManager.enqueue(request)
//
//        // Show a toast message to indicate that the download has started
//        Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show()
//    }

    // Function to extract file name from URL
    private fun getFileNameFromUrl(url: String): String {
        // Get the file name from the URL
        val fileNameStartIndex = url.lastIndexOf('/') + 1
        val fileName = url.substring(fileNameStartIndex)

        // Extract file extension from the file name
        val extensionIndex = fileName.lastIndexOf('.')
        val extension = if (extensionIndex != -1) fileName.substring(extensionIndex + 1) else ""

        // Return a unique file name with extension
        return "document.${extension}"
    }

    private fun getMimeType(file: File): String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.absolutePath)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "*/*"
    }
}