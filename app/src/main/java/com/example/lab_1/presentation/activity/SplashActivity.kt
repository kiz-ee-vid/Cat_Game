package com.example.lab_1.presentation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.lab_1.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity_screen)

        val backgroundImage: ImageView = findViewById(R.id.imageView)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.splash)
        backgroundImage.startAnimation(slideAnimation)

        Handler().postDelayed({

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        },2000)
    }
}