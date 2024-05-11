package com.ankur.admin_notifycampus.Years

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.ankur.admin_notifycampus.Models.FacultyModel
import com.ankur.admin_notifycampus.adapters.RemoveFacultyAdapter
import com.ankur.admin_notifycampus.adapters.SwipeToDeleteCallback2
import com.example.notify.databinding.ActivityYearBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class YearActivity : AppCompatActivity() {

    private lateinit var binding: ActivityYearBinding
    private lateinit var  collection: String
    private val TAG="year"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityYearBinding.inflate(layoutInflater)
        setContentView(binding.root)

        collection=intent.getStringExtra("year").toString()
        Log.d(TAG,"collection is $collection")
        getFaculty()
    }

    private fun getFaculty() {

        val list = ArrayList<FacultyModel>()
        lifecycleScope.launch(Dispatchers.IO){
            Firebase.firestore.collection(collection).get().await()
                .also {
                    for (doc in it.documents){
                        val data = doc.toObject(FacultyModel::class.java)
                        Log.d(TAG,"data : $data")
                        list.add(data!!)
                        Log.d(TAG,"list : $list")

                    }
                }

            withContext(Dispatchers.Main){
                val adapter = RemoveFacultyAdapter(this@YearActivity,list,collection)
                binding.recyclerView.adapter=adapter
                val swipeToDeleteCallback = SwipeToDeleteCallback2(adapter, collection,binding.recyclerView)
                val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
                itemTouchHelper.attachToRecyclerView(binding.recyclerView)
            }
        }
    }
}