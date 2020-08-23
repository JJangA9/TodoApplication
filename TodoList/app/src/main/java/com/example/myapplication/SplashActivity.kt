package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity(){
    private val SPLASH_TIME_OUT:Long = 1000
    private val WRITING_TIME_OUT:Long = 500
    override fun onCreate(savedInstances: Bundle?) {
        super.onCreate(savedInstances)
        setContentView(R.layout.activity_splash);
        val imageView2 = findViewById<ImageView>(R.id.splashW2)
        imageView2.visibility = View.INVISIBLE

        //투두의 빨간색 글씨 1초후 나타내기
        Handler().postDelayed({
                imageView2.visibility = View.VISIBLE
            }, WRITING_TIME_OUT)

        //2초후 mainActivity로 전환
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}