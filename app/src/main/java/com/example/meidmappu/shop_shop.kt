package com.example.meidmappu

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView


class shop_shop : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_shop)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //うけとる
        val name = intent.getStringExtra("shopName")
        val address = intent.getStringExtra("shopAddress")
        val image1Id = intent.getIntExtra("image1", 0)
        val image2Id = intent.getIntExtra("image2", 0)


        findViewById<TextView>(R.id.shop_name).text = name
        val mainImage: ImageView = findViewById(R.id.imageView4)
        val menuImage: ImageView = findViewById(R.id.imageView5)

        // 3. 画像を設定 (IDが0でない場合のみsetImageResourceを実行)
        if (image1Id != 0) {
            mainImage.setImageResource(image1Id)
        }
        if (image2Id != 0) {
            menuImage.setImageResource(image2Id)
        }

    }
}