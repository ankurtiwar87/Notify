package com.ankur.admin_notifycampus.Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.ankur.admin_notifycampus.Models.NoticeModel
import com.ankur.admin_notifycampus.pushNotification.NotificationData
import com.ankur.admin_notifycampus.pushNotification.PushNotification
import com.ankur.admin_notifycampus.pushNotification.RetrofitInstance
import com.ankur.admin_notifycampus.pushNotification.TOPIC
import com.example.notify.R
import com.example.notify.databinding.ActivityAddNoticeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddNotice : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoticeBinding
    val TAG = "PushNotification"


    private val storage = Firebase.storage
    private val storageRef = storage.reference

//    This URI is for Image from Gallery
    private var coverImage: Uri? = null


    //    This is for the firebase storage url
    private var coverImageUrl:String ?= ""
    private var docUrl:String ?= ""

    private var spinnerYear:String=""
    private lateinit var dialog: Dialog

//    This is used to went to gallery
    private var launchGalleyActivity =registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        if (it.resultCode== Activity.RESULT_OK)
        {
            coverImage=it.data!!.data
            binding.noticeImage.setImageURI(coverImage)
        }
    }

//    Document Get code
private val chooseFileButton by lazy { findViewById<Button>(R.id.chooseFileButton) }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { uri ->
                    uploadFile(uri)
                }
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog= Dialog(this)
        dialog.setContentView(R.layout.progress_dialog)
        dialog.setCancelable(false)

        //Spinner setUp
        val category = resources.getStringArray(R.array.session)

        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,category)
        binding.spinner.adapter=adapter

        binding.spinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerYear=category[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@AddNotice, "Select Category", Toast.LENGTH_SHORT).show()
            }

        }

         // Open the Gallery on click
//        binding.noticeImage.setOnClickListener {
//            val intent = Intent("android.intent.action.GET_CONTENT")
//            intent.type="image/*"
//            launchGalleyActivity.launch(intent)
//        }

        binding.noticeImage.setOnClickListener {
            val options = arrayOf("Open Gallery", "Take Photo")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose an option")
            builder.setItems(options) { dialog, which ->
                when (which) {
                    0 -> openGallery()
                    1 -> openCamera()
                }
                dialog.dismiss()
            }
            builder.show()
        }


        binding.chooseFileButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*" // Allow all file types
            getContent.launch(intent)
        }


        binding.uploadNotice.setOnClickListener {
            validateData()
        }



    }


    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera app available
        if (intent.resolveActivity(packageManager) != null) {
            // Create a file to save the image
            val photoFile: File? = createImageFile()
            photoFile?.let {
                coverImage = FileProvider.getUriForFile(this, "com.example.notify.provider", it)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, coverImage)
                val REQUEST_IMAGE_CAPTURE=121
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFileName = "JPEG_${timeStamp}_"
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val REQUEST_IMAGE_CAPTURE=121
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Use the photoUri to set the image in the ImageView
            coverImage?.let { uri ->
                binding.noticeImage.setImageURI(uri)
            }
        }
    }


    private fun openGallery() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launchGalleyActivity.launch(intent)
    }

    private fun validateData() {

        if(binding.title.text.toString().isEmpty()){
            binding.title.error="Add Title"
            binding.title.requestFocus()
        }
        if (binding.description.text.toString().isEmpty()){
            binding.description.error="Add Description"
            binding.description.requestFocus()
        }
        if (coverImage==null){
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show()
        }
        else{
            uploadImage()
        }

    }

    private fun uploadImage() {
        dialog.show()

        val fileName = UUID.randomUUID().toString()+".jpg"

        val refStorage = storageRef.child("Notice Images").child("Notice/$fileName")
        refStorage.putFile(coverImage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { Image  ->
//                    Data to be upload on firebase
                    coverImageUrl=Image.toString()

                    uploadData()
                }
            }.addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(this, "Something went wrong with Storage", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadData() {

        dialog.show()

//        for date
        val callForDate = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd-MM-yy")
        val date = currentDate.format(callForDate.time)
//        for time
        val callForTime = Calendar.getInstance()
        val currentTime = SimpleDateFormat("hh:mm a")
        val time = currentTime.format(callForTime.time)

        val db = Firebase.firestore.collection(spinnerYear)
        val id = db.document().id



        val data = NoticeModel(
            id,
            title = binding.title.text.toString(),
            desc = binding.description.text.toString(),
            imgUrl = coverImageUrl,
           docUrl= docUrl,
            date =date,
           time= time,
           collectionName =  spinnerYear

        )

        lifecycleScope.launch(Dispatchers.IO) {
            db.document(id).set(data).await()

            withContext(Dispatchers.Main){
                dialog.dismiss()
                binding.chooseFileButton.text="CHOOSE FILE"
                binding.noticeImage.setImageResource(R.drawable.placeholder)
                binding.title.text=null
                binding.description.text=null
                Toast.makeText(applicationContext, "Notice Uploaded", Toast.LENGTH_SHORT).show()
            }
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
            val title = "New Notice"
            val message = "A New Notice Added For Session ${spinnerYear} at $time"
            if(!message.isNullOrEmpty()){
                PushNotification(
                    NotificationData(title,message),
                    TOPIC
                ).also {
                    sendNotification(it)
                }
            }
        }


    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }


//    private fun uploadFile(uri: Uri) {
//
//        dialog.show()
//
//        // Generate a unique file name (you can modify this logic as needed)
//        val fileName = getFileNameFromUri(uri)+"${System.currentTimeMillis()}" // Get the original file name from the URI
//
////        val fileName = "file_${System.currentTimeMillis()}"
//        val fileRef = storageRef.child("Documents").child(fileName)
//
//        // Upload the file to Firebase Storage
//        val uploadTask = fileRef.putFile(uri)
//
//        // Listen for state changes, errors, and completion of the upload
//        uploadTask.addOnSuccessListener { taskSnapshot ->
//            // Upload successful
//            taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener{
//                docUrl=it.toString()
//
//            }
//            dialog.dismiss()
//            binding.chooseFileButton.text="$fileName"
//            // Handle the download URL as needed
//        }.addOnFailureListener { exception ->
//            // Handle unsuccessful uploads
//            Toast.makeText(this, "Doc not Uploaded", Toast.LENGTH_SHORT).show()
//            dialog.dismiss()
//        }
//    }

    private fun uploadFile(uri: Uri) {
        dialog.show()

        // Get the original file name and extension from the URI
        val fileName = getFileNameFromUri(uri)
        val extension = getFileExtensionFromUri(uri)

        // Generate a unique file name by appending the current timestamp and original extension
        val uniqueFileName = "${fileName}_${System.currentTimeMillis()}.$extension"
        Toast.makeText(this, "file name $uniqueFileName", Toast.LENGTH_SHORT).show()
        val fileRef = storageRef.child("Documents").child(uniqueFileName)

        // Upload the file to Firebase Storage
        val uploadTask = fileRef.putFile(uri)

        // Listen for state changes, errors, and completion of the upload
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Upload successful
            taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener{
                docUrl=it.toString()
            }
            dialog.dismiss()
            binding.chooseFileButton.text = uniqueFileName
            // Handle the download URL as needed
        }.addOnFailureListener { exception ->
            // Handle unsuccessful uploads
            Toast.makeText(this, "Doc not Uploaded", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }

    // Function to get the file extension from the URI
    private fun getFileExtensionFromUri(uri: Uri): String {
        return MimeTypeMap.getFileExtensionFromUrl(uri.toString()) ?: ""
    }

    @SuppressLint("Range")
    private fun getFileNameFromUri(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                return it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        // If we can't retrieve the file name, generate a unique name here
        return "file_${System.currentTimeMillis()}"
    }
}