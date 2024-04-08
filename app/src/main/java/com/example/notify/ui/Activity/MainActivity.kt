package com.example.notify.ui.Activity

import NoticeViewModelFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.notify.R
import com.example.notify.Room.NoticeDatabase
import com.example.notify.databinding.ActivityMainBinding
import com.example.notify.repository.NoticeRepository
import com.example.notify.viewModel.NoticeViewModel

class MainActivity : AppCompatActivity() {
    private  var _binding:ActivityMainBinding?=null
    private val binding get() = _binding!!
    lateinit var viewModel:NoticeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val databaseDaoInstance = NoticeDatabase.getDatabase(this).getNoticeDao()
        val repository= NoticeRepository(databaseDaoInstance)
//        val repository=NoticeRepository()
        viewModel= ViewModelProvider(this,NoticeViewModelFactory(repository)).get(
            NoticeViewModel::class.java)

        val navController = findNavController(R.id.fragmentContainerView)
        binding.bottomNavigationView.setupWithNavController(navController)
    }


    override fun onDestroy() {
        super.onDestroy()
        // Release the binding when the view is destroyed to avoid memory leaks
        _binding = null
    }

}