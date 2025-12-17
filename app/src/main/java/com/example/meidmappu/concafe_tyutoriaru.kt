package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class concafe_tyutoriaru : AppCompatActivity() {

    private var currentIndex = 0

    // チュートリアル画像リスト
    private val concafeImages = arrayOf(
        R.drawable.cafe0,
        R.drawable.cafe1, R.drawable.cafe2, R.drawable.cafe3, R.drawable.cafe4, R.drawable.cafe5,
        R.drawable.cafe6, R.drawable.cafe7, R.drawable.cafe8, R.drawable.cafe9, R.drawable.cafe10,
        R.drawable.cafe11, R.drawable.cafe12, R.drawable.cafe13, R.drawable.cafe14, R.drawable.cafe15,
        R.drawable.cafe16, R.drawable.cafe17, R.drawable.cafe18, R.drawable.cafe19, R.drawable.cafe20,
        R.drawable.cafe21, R.drawable.cafe22, R.drawable.cafe23, R.drawable.cafe24, R.drawable.cafe25,
        R.drawable.cafe26, R.drawable.cafe27, R.drawable.cafe28, R.drawable.cafe29, R.drawable.cafe30,
        R.drawable.cafe31, R.drawable.cafe32, R.drawable.cafe33, R.drawable.cafe34, R.drawable.cafe35,
        R.drawable.cafe36, R.drawable.cafe37, R.drawable.cafe38, R.drawable.cafe39
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concafe_tyutoriaru)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        val tutorialImage = findViewById<ImageView>(R.id.concafeImage)

        // 最初の画像
        tutorialImage.setImageResource(concafeImages[currentIndex])

        // 画像タップで次へ
        tutorialImage.setOnClickListener {
            currentIndex++
            if (currentIndex < concafeImages.size) {
                tutorialImage.setImageResource(concafeImages[currentIndex])
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // ホームに戻る
        val homeback3 = findViewById<ImageButton>(R.id.homeback3)
        homeback3.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
