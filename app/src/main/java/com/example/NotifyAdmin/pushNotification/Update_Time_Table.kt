package com.ankur.admin_notifycampus.pushNotification

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.notify.databinding.ActivityUpdateTimeTableBinding
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic2"
class Update_Time_Table : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTimeTableBinding
    val TAG = "PushNotification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUpdateTimeTableBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
//        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
//            FirebaseService.token = it.token
//            etToken.setText(it.token)
//        }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        binding.uploadNotice.setOnClickListener {
            val title = "Change Faculty"
            val year=binding.year.text.toString()
            val section=binding.section.text.toString()
            val assignedFaculty = binding.assignedFaculty.text.toString()
            val newFaculty = binding.newFaculty.text.toString()
            val lectureNumber = binding.lectureNumber.text.toString()


            val message = "Student of ${year} Year Section ${section} informed that faculty ${assignedFaculty} is changed with $newFaculty for lecture ${lectureNumber} "

            if(!year.isNullOrEmpty() && !section.isNullOrEmpty()&& !assignedFaculty.isNullOrEmpty()&& !newFaculty.isNullOrEmpty()&& !lectureNumber.isNullOrEmpty()){
                PushNotification(
                    NotificationData(title,message),
                    TOPIC
                ).also {
                    sendNotification(it)
                }
            }
            finish()
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
}