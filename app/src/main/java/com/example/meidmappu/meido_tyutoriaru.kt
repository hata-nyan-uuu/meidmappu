package com.example.meidmappu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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


        val tutorialImage = findViewById<ImageView>(R.id.maidImage)
        tutorialImage.setBackgroundColor(android.graphics.Color.WHITE)
        // 最初の画像設定
        tutorialImage.setImageResource(meidoImages[currentIndex])

        // タップで次へ
        findViewById<Button>(R.id.nextbtn).setOnClickListener {
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
        //ホーム画面に戻る
        val homeback=findViewById<ImageButton>(R.id.homeback)
        homeback.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //ひとつ前もどる
        val backTouchArea = findViewById<ImageButton>(R.id.backTouchArea)

        backTouchArea.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                tutorialImage.setImageResource(meidoImages[currentIndex])
            } else {
                finish()
            }
        }

    }
}
