package com.example.notify.ui.Activity

import NoticeViewModelFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ankur.admin_notifycampus.pushNotification.TOPIC
import com.example.notify.R
import com.example.notify.Room.NoticeDatabase
import com.example.notify.databinding.ActivityMainBinding
import com.example.notify.databinding.NotifyActivityMainBinding
import com.example.notify.repository.NoticeRepository
import com.example.notify.viewModel.NoticeViewModel
import com.google.firebase.messaging.FirebaseMessaging


class NotifyMainActivity : AppCompatActivity() {
    private  var _binding:NotifyActivityMainBinding?=null
    private val binding get() = _binding!!
    lateinit var viewModel:NoticeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= NotifyActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        val databaseDaoInstance = NoticeDatabase.getDatabase(this).getNoticeDao()
        val repository= NoticeRepository(databaseDaoInstance)
//        val repository=NoticeRepository()
        viewModel= ViewModelProvider(this,NoticeViewModelFactory(repository)).get(
            NoticeViewModel::class.java)

        val navController = findNavController(R.id.fragmentContainerView)
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.noticeFragment || destination.id == R.id.facultyFragment || destination.id == R.id.about_Us_Fragment) {
                binding.bottomNavigationView.visibility = View.VISIBLE
            } else {
                binding.bottomNavigationView.visibility = View.GONE
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        // Release the binding when the view is destroyed to avoid memory leaks
        _binding = null
    }

}