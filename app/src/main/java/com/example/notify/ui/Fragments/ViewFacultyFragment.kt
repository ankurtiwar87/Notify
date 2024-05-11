package com.example.notify.ui.Fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.notify.databinding.FragmentViewFacultyBinding
import com.example.notify.ui.Activity.NotifyMainActivity
import com.example.notify.ui.adapters.FacultyAdapter
import com.example.notify.ui.adapters.FacultyAdapterOffline
import com.example.notify.viewModel.NoticeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewFacultyFragment : Fragment() {

    private var _binding: FragmentViewFacultyBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoticeViewModel


    private val args:ViewNoticeFragmentArgs by navArgs()
    private lateinit var collectionName:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentViewFacultyBinding.inflate(inflater,container,false)
        viewModel=(activity as NotifyMainActivity).viewModel
        collectionName=args.collection


        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        lifecycleScope.launch(Dispatchers.IO) {
            val isConnected = isNetworkAvailable(connectivityManager)
            if (isConnected) {
                val list = viewModel.fetchFaculty(collectionName)
                withContext(Dispatchers.Main) {
                    binding.recyclerView.adapter = FacultyAdapter(requireContext(), list)
                }
            } else {
                val list = viewModel.getOfflineFaculty(collectionName)
                withContext(Dispatchers.Main) {
                    binding.recyclerView.adapter = FacultyAdapterOffline(requireContext(), list)
                }
            }
        }
        return binding.root
    }



    private fun isNetworkAvailable(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}