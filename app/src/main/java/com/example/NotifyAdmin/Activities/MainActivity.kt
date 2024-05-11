package com.ankur.admin_notifycampus.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ankur.admin_notifycampus.Sessions.SessionTemplate
import com.ankur.admin_notifycampus.Years.YearTemplate
import com.ankur.admin_notifycampus.pushNotification.Update_Time_Table
import com.example.NotifyAdmin.Activities.DeveloperFeedbackActivity
import com.example.notify.LoginAndSignIn.LoginActivity
import com.example.notify.LoginAndSignIn.SignupActivity
import com.example.notify.R
import com.example.notify.databinding.ActivityMainBinding

private const val PREF_NAME = "LoginPrefs"
private const val KEY_USERNAME = "username"
private const val KEY_PASSWORD = "password"
private const val KEY_ROLE = "role"
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addNotice.setOnClickListener(this)
        binding.addFaculty.setOnClickListener(this)
        binding.deleteNotice.setOnClickListener(this)
        binding.deleteFaculty.setOnClickListener(this)
        binding.AddRole.setOnClickListener(this)
        binding.feedbackDeveloper.setOnClickListener(this)

        val sharedPreferences = getSharedPreferences(com.ankur.admin_notifycampus.pushNotification.PREF_NAME, Context.MODE_PRIVATE)
        val role = sharedPreferences.getString(com.ankur.admin_notifycampus.pushNotification.KEY_ROLE, null)

        if (role=="Developer"){
            binding.cardView5.visibility=View.VISIBLE
            binding.cardView6.visibility=View.VISIBLE
        }

        binding.updateTimeTable.setOnClickListener {
            val intent =Intent(this, Update_Time_Table::class.java)
            startActivity(intent)
        }

        binding.logoutAdminDev.setOnClickListener {
            val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            val intent =Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    override fun onClick(v: View?) {

        if (v != null) {
            when(v.id){

                R.id.add_notice ->{
                    val intent = Intent(this,AddNotice::class.java)
                    startActivity(intent)
                }
                R.id.addFaculty ->{
                    val intent = Intent(this,AddFaculty::class.java)
                    startActivity(intent)
                }
                R.id.deleteNotice->{
                    val intent=Intent(this,SessionTemplate::class.java)
                    startActivity(intent)
                }
                R.id.deleteFaculty->{
                    val intent =Intent(this,YearTemplate::class.java)
                    startActivity(intent)
                }
                R.id.AddRole->{
                    val intent=Intent(this, SignupActivity::class.java)
                    startActivity(intent)
                }
                R.id.feedbackDeveloper->{
                    val intent=Intent(this,DeveloperFeedbackActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }
}