package com.ankur.admin_notifycampus.Years

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.example.notify.R
import com.example.notify.databinding.ActivityYearTemplateBinding

class YearTemplate : AppCompatActivity(),OnClickListener{

    private lateinit var binding: ActivityYearTemplateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityYearTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cardView1.setOnClickListener(this)
        binding.cardView2.setOnClickListener(this)
        binding.cardView3.setOnClickListener(this)
        binding.cardView4.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v!=null){
            when(v.id){
                R.id.cardView1->{
                    val i = Intent(this,YearActivity::class.java)
                    i.putExtra("year","1")
                    startActivity(i)
                }
                R.id.cardView2->{
                    val i = Intent(this,YearActivity::class.java)
                    i.putExtra("year","2")
                    startActivity(i)
                }
                R.id.cardView3->{
                    val i = Intent(this,YearActivity::class.java)
                    i.putExtra("year","3")
                    startActivity(i)
                }  R.id.cardView4->{
                val i = Intent(this,YearActivity::class.java)
                i.putExtra("year","4")
                startActivity(i)
            }

            }
        }
    }
}