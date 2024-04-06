package com.example.notify.ui.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.notify.R
import com.example.notify.databinding.FragmentNoticeBinding
import com.example.notify.viewModel.NoticeViewModel

class NoticeFragment : Fragment() {

    private var _binding:FragmentNoticeBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoticeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentNoticeBinding.inflate(inflater,container,false)

        binding.cardView1.setOnClickListener {
            val action= NoticeFragmentDirections.actionNoticeFragmentToViewNoticeFragment(
                collection = "2019-2023"
            )
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.cardView2.setOnClickListener {
            val action= NoticeFragmentDirections.actionNoticeFragmentToViewNoticeFragment(
                collection = "2020-2024"
            )
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.cardView3.setOnClickListener {
            val action= NoticeFragmentDirections.actionNoticeFragmentToViewNoticeFragment(
                collection = "2021-2025"
            )
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.cardView4.setOnClickListener {
            val action= NoticeFragmentDirections.actionNoticeFragmentToViewNoticeFragment(
                collection = "2022-2026"
            )
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.cardView5.setOnClickListener {
            val action= NoticeFragmentDirections.actionNoticeFragmentToViewNoticeFragment(
                collection = "2023-2027"
            )
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.cardView6.setOnClickListener {
            val action= NoticeFragmentDirections.actionNoticeFragmentToViewNoticeFragment(
                collection = "2024-2028"
            )
            Navigation.findNavController(binding.root).navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}