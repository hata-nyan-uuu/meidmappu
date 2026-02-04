package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {

    private val splashTime = 2000L // 2秒
    private var isFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val root = findViewById<View>(R.id.splashRoot)

        // タップで即スキップ
        root.setOnClickListener {
            goMain()
        }

        // 時間経過で遷移
        root.postDelayed({
            goMain()
        }, splashTime)
    }

    private fun goMain() {
        if (isFinished) return
        isFinished = true

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
