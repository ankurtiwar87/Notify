package com.example.notify.ui.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.notify.R
import com.example.notify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private  var _binding:ActivityMainBinding?=null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.fragmentContainerView)
        binding.bottomNavigationView.setupWithNavController(navController)
    }


    override fun onDestroy() {
        super.onDestroy()
        // Release the binding when the view is destroyed to avoid memory leaks
        _binding = null
    }

}