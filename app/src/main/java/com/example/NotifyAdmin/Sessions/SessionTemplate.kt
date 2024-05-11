package com.ankur.admin_notifycampus.Sessions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.example.notify.R

import com.example.notify.databinding.ActivitySessionTemplateBinding

class SessionTemplate : AppCompatActivity(),OnClickListener{
    private lateinit var binding: ActivitySessionTemplateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySessionTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cardView1.setOnClickListener(this)
        binding.cardView2.setOnClickListener(this)
        binding.cardView3.setOnClickListener(this)
        binding.cardView4.setOnClickListener(this)
        binding.cardView5.setOnClickListener(this)
        binding.cardView6.setOnClickListener(this)
//        binding.cardView7.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v!=null){
            when(v.id){
                R.id.cardView1->{
                    val i =Intent(this,Session::class.java)
                    i.putExtra("session","2019-2023")
                    startActivity(i)
                }
                R.id.cardView2->{
                    val i =Intent(this,Session::class.java)
                    i.putExtra("session","2020-2024")
                    startActivity(i)
                }
                R.id.cardView3->{

                    val i =Intent(this,Session::class.java)
                    i.putExtra("session","2021-2025")
                    startActivity(i)
                }
                R.id.cardView4->{

                    val i =Intent(this,Session::class.java)
                    i.putExtra("session","2022-2026")
                    startActivity(i)
                }
                R.id.cardView5->{

                    val i =Intent(this,Session::class.java)
                    i.putExtra("session","2023-2027")
                    startActivity(i)
                }
                R.id.cardView6->{

                    val i =Intent(this,Session::class.java)
                    i.putExtra("session","2024-2028")
                    startActivity(i)
                }
//                R.id.cardView7->{
//
//                    val i =Intent(this,Session::class.java)
//                    i.putExtra("session","everyone")
//                    startActivity(i)
//                }
            }
        }
    }
}