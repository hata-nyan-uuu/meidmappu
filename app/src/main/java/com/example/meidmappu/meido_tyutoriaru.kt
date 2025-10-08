package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class meido_tyutoriaru : AppCompatActivity() {

    private var currentIndex = 0

    // メイドカフェチュートリアル画像
    private val meidoImages = arrayOf(
        R.drawable.tyutoriaru04,
        R.drawable.tyutoriaru06,
        R.drawable.tyutoriaru07,
        R.drawable.tyutoriaru08,
        R.drawable.tyutoriaru09,
        R.drawable.tyutoriaru10,
        R.drawable.tyutoriaru11,
        R.drawable.tyutoriaru11_5,
        R.drawable.tyutoriaru12,
        R.drawable.tyutoriaru13,
        R.drawable.tyutoriaru14,
        R.drawable.tyutoriaru15,
        R.drawable.tyutoriaru16,
        R.drawable.tyutoriaru17,
        R.drawable.tyutoriaru18,
        R.drawable.tyutoriaru19,
        R.drawable.tyutoriaru20,
        R.drawable.tyutoriaru21,
        R.drawable.tyutoriaru22,
        R.drawable.tyutoriaru23,
        R.drawable.tyutoriaru24,
        R.drawable.tyutoriaru25,
        R.drawable.tyutoriaru26,
        R.drawable.tyutoriaru27
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_meido_tyutoriaru)

        // ステータスバーなどの余白調整
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nextButton = findViewById<Button>(R.id.nextbtn04)
        val layout = findViewById<ConstraintLayout>(R.id.main)

        // 最初の画像をセット
        layout.setBackgroundResource(meidoImages[currentIndex])

        // タップで次の画像へ
        nextButton.setOnClickListener {
            currentIndex++
            if (currentIndex < meidoImages.size) {
                layout.setBackgroundResource(meidoImages[currentIndex])
            } else {
                // 全部見終わったら終了（または次の画面へ）
                finish()
            }
        }
    }
}