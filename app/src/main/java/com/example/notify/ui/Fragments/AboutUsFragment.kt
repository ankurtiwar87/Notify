package com.example.notify.ui.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.notify.databinding.FragmentAboutUsBinding
import com.example.notify.ui.Activity.AboutActivity
import com.example.notify.ui.Activity.MainActivity
import com.example.notify.ui.Activity.PrivacyPolicyActivity
import com.example.notify.viewModel.NoticeViewModel


class AboutUsFragment : Fragment() {

    private lateinit var binding: FragmentAboutUsBinding
    private lateinit var viewModel: NoticeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAboutUsBinding.inflate(inflater,container,false)

        viewModel=(activity as MainActivity).viewModel

        binding.cardAbout.setOnClickListener{

            val iAbout = Intent(activity, AboutActivity::class.java)
            startActivity(iAbout)
        }

        binding.cardPolicy.setOnClickListener {

            val iPolicy = Intent(activity, PrivacyPolicyActivity ::class.java)
            startActivity(iPolicy)
        }

        binding.cardRate.setOnClickListener {

            val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.maverickbits.statussaverpro&pcampaignid=web_share")
            val iRate = Intent(Intent.ACTION_VIEW,uri)
            startActivity(iRate)
        }

        binding.cardShare.setOnClickListener {

            val iShare = Intent(Intent.ACTION_SEND)
            iShare.type = "text/plain"
            iShare.putExtra(Intent.EXTRA_TEXT,"Share this amazing App: https://play.google.com/store/apps/details?id=com.maverickbits.statussaverpro&pcampaignid=web_share")
            val chooser = Intent.createChooser(iShare, "Share Via")
            startActivity(chooser)
        }

        binding.feedback.setOnClickListener {

            val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.maverickbits.statussaverpro&pcampaignid=web_share")
            val iRate = Intent(Intent.ACTION_VIEW,uri)
            startActivity(iRate)
        }

//        binding.cardLogout.setOnClickListener {
//
//            Toast.makeText(requireContext(),"Coming Soon", Toast.LENGTH_SHORT ).show()
//        }

        return binding.root

    }
}