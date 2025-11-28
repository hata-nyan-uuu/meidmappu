package com.example.meidmappu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class meido_tyutoriaru : AppCompatActivity() {

    private var currentIndex = 0

    // チュートリアル画像リスト（必要に応じて追加）
    private val meidoImages = arrayOf(
        R.drawable.maid0,
        R.drawable.maid1,
        R.drawable.maid2,
        R.drawable.maid3,
        R.drawable.maid4,
        R.drawable.maid5,
        R.drawable.maid6,
        R.drawable.maid7,
        R.drawable.maid8,
        R.drawable.maid9,
        R.drawable.maid10,
        R.drawable.maid11,
        R.drawable.maid12,
        R.drawable.maid13,
        R.drawable.maid14,
        R.drawable.maid15,
        R.drawable.maid16,
        R.drawable.maid17,
        R.drawable.maid18,
        R.drawable.maid19,
        R.drawable.maid20,
        R.drawable.maid21,
        R.drawable.maid22,
        R.drawable.maid23,
        R.drawable.maid24,
        R.drawable.maid25,
        R.drawable.maid26,
        R.drawable.maid27,
        R.drawable.maid28,
        R.drawable.maid29,
        R.drawable.maid30,
        R.drawable.maid31,
        R.drawable.maid32,
        R.drawable.maid33,
        R.drawable.maid34,
        R.drawable.maid35,
        R.drawable.maid36

    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_meido_tyutoriaru)

        val tutorialImage = findViewById<ImageView>(R.id.maidImage)

        // 最初の画像設定
        tutorialImage.setImageResource(meidoImages[currentIndex])

        // タップで次へ
        tutorialImage.setOnClickListener {
            currentIndex++
            if (currentIndex < meidoImages.size) {
                tutorialImage.setImageResource(meidoImages[currentIndex])
            } else {
                // 最後まで見たら閉じる（または次画面へ）
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
