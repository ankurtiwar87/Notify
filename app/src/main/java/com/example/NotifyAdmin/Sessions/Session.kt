package com.ankur.admin_notifycampus.Sessions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.ankur.admin_notifycampus.Models.NoticeModel
import com.ankur.admin_notifycampus.adapters.RemoveNoticeAdapter
import com.ankur.admin_notifycampus.adapters.SwipeToDeleteCallback
import com.example.notify.databinding.ActivitySession23Binding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Session : AppCompatActivity() {

    private lateinit var binding: ActivitySession23Binding
    private lateinit var collection: String
    private val TAG = "session"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySession23Binding.inflate(layoutInflater)
        setContentView(binding.root)

        collection = intent.getStringExtra("session").toString()
        Log.d(TAG, "collection is $collection")
        getSession23Notice()
    }

    private fun getSession23Notice() {
        lifecycleScope.launch(Dispatchers.IO) {
            val list = ArrayList<NoticeModel>()
            try {
                Firebase.firestore.collection(collection).get().await().forEach { doc ->
                    val data = doc.toObject(NoticeModel::class.java)
                    list.add(data!!)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting notices: ${e.message}")
            }
            withContext(Dispatchers.Main) {
                val adapter = RemoveNoticeAdapter(this@Session, list, collection)
                binding.recyclerView.adapter = adapter
                val swipeToDeleteCallback = SwipeToDeleteCallback(adapter, collection,binding.recyclerView)
                val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
                itemTouchHelper.attachToRecyclerView(binding.recyclerView)            }
        }
    }
}