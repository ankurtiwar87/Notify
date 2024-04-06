package com.example.notify.ui.Fragments

import NoticeViewModelFactory
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.notify.Room.NoticeDatabase
import com.example.notify.databinding.FragmentViewNoticeBinding
import com.example.notify.repository.NoticeRepository
import com.example.notify.ui.adapters.NoticeAdapter
import com.example.notify.ui.adapters.NoticeAdapterOffline
import com.example.notify.viewModel.NoticeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewNoticeFragment : Fragment() {
    private val TAG="ViewNoticeFragment"
    private var _binding: FragmentViewNoticeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NoticeViewModel
    private val args:ViewNoticeFragmentArgs by navArgs()
    private lateinit var collectionName:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        collectionName =args.collection
        Log.d(TAG,"collectionName $collectionName")
        _binding=FragmentViewNoticeBinding.inflate(inflater,container,false)
        val databaseDaoInstance = NoticeDatabase.getDatabase(requireContext()).getNoticeDao()
        val repository= NoticeRepository(databaseDaoInstance)
//        val repository=NoticeRepository()
        viewModel=ViewModelProvider(requireActivity(),NoticeViewModelFactory(repository)).get(NoticeViewModel::class.java)


        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        lifecycleScope.launch(Dispatchers.IO) {
            val isConnected = isNetworkAvailable(connectivityManager)
            if (isConnected) {
                val list = viewModel.fetchNotices(collectionName)
                withContext(Dispatchers.Main) {
                    binding.recyclerView.adapter = NoticeAdapter(requireContext(), list)
                }
            } else {
                val list = viewModel.getOfflineNotices(collectionName)
                withContext(Dispatchers.Main) {
                    binding.recyclerView.adapter = NoticeAdapterOffline(requireContext(), list)
                }
            }
        }

//        lifecycleScope.launch(Dispatchers.IO){
//           val list= viewModel.fetchNotices(collectionName)
//            withContext(Dispatchers.Main){
//                binding.recyclerView.adapter=NoticeAdapter(requireContext(),list)
//            }
//
//        }
//
//        lifecycleScope.launch(Dispatchers.IO){
//            val list= viewModel.getOfflineNotices(collectionName)
//            withContext(Dispatchers.Main){
//                binding.recyclerView.adapter=NoticeAdapterOffline(requireContext(),list)
//            }
//
//        }




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }


    override fun onDestroyView() {
        super.onDestroyView()
        // Release the binding when the view is destroyed to avoid memory leaks
        _binding = null
    }

    private fun isNetworkAvailable(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }


}