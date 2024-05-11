package com.example.notify.ui.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notify.LoginAndSignIn.LoginActivity
import com.example.notify.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScrren : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_scrren)
        CoroutineScope(Dispatchers.Main).launch {
            delay(500L)
            val intent=Intent(this@SplashScrren, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}