package com.example.notify.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.notify.R
import com.example.notify.databinding.FragmentFacultyBinding
import com.example.notify.databinding.FragmentNoticeBinding
import com.example.notify.ui.Activity.MainActivity
import com.example.notify.viewModel.NoticeViewModel

class FacultyFragment : Fragment() {

    private var _binding: FragmentFacultyBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoticeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentFacultyBinding.inflate(inflater,container,false)
        viewModel=(activity as MainActivity).viewModel
        binding.cardView1.setOnClickListener {
            val action=FacultyFragmentDirections.actionFacultyFragmentToViewFacultyFragment(
                collection = "1"
            )

            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.cardView2.setOnClickListener {
            val action=FacultyFragmentDirections.actionFacultyFragmentToViewFacultyFragment(
                collection = "2"
            )

            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.cardView3.setOnClickListener {
            val action=FacultyFragmentDirections.actionFacultyFragmentToViewFacultyFragment(
                collection = "3"
            )

            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.cardView4.setOnClickListener {
            val action=FacultyFragmentDirections.actionFacultyFragmentToViewFacultyFragment(
                collection = "4"
            )

            Navigation.findNavController(binding.root).navigate(action)
        }
        return binding.root
    }

}