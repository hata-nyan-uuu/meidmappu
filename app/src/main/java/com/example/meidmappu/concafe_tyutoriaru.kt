package com.example.meidmappu

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

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
        enableEdgeToEdge()
        setContentView(R.layout.activity_meido_tyutoriaru)

        val tutorialImage = findViewById<ImageView>(R.id.maidImage)

        // 最初の画像設定
        tutorialImage.setImageResource(concafeImages[currentIndex])

        // タップで次へ
        tutorialImage.setOnClickListener {
            currentIndex++
            if (currentIndex < concafeImages.size) {
                tutorialImage.setImageResource(concafeImages[currentIndex])
            } else {
                finish() // 全部見たら終了
            }
        }
    }
}
