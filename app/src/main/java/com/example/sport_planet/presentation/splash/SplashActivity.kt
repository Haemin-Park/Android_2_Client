package com.example.sport_planet.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.sport_planet.R
import com.example.sport_planet.databinding.ActivitySplashBinding
import com.example.sport_planet.presentation.base.BaseActivity
import com.example.sport_planet.presentation.login.LoginActivity
import com.example.sport_planet.presentation.splash.IntroActivity.Companion.SPLASH

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    companion object {
        private const val SPLASH_DELAY_TIME: Long = 1500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            if (getSharedPreferences(SPLASH, MODE_PRIVATE).getBoolean(SPLASH, false)) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

        }, SPLASH_DELAY_TIME)
    }
}