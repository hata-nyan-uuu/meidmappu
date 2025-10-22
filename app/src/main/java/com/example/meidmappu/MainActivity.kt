package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    // 画像と対応する店舗名をリストで管理
    private val shopImages = listOf(R.id.omise01, R.id.omise02, R.id.omise03)
    private val shopNames = listOf("カフェ東京", "寿司太郎", "パン工房花")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Edge-to-Edge 設定
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // データベースを初期化
        db = AppDatabase.getDatabase(this)


        // 画像クリックで店舗詳細を開く
        shopImages.forEachIndexed { index, imageId ->
            findViewById<ImageView>(imageId).setOnClickListener {
                openShopDetail(shopNames[index])
            }
        }



        // 既存のボタン処理
        val random1 = findViewById<Button>(R.id.random1)
        random1.setOnClickListener {
            startActivity(Intent(this, random_kensaku::class.java))
        }

        val kodawari = findViewById<Button>(R.id.kodawari)
        kodawari.setOnClickListener {
            startActivity(Intent(this, kodawari_kensaku::class.java))
        }

        val hazimete = findViewById<Button>(R.id.hazimete)
        hazimete.setOnClickListener {
            startActivity(Intent(this, tyutoriaru01::class.java))
        }
    }

    private fun openShopDetail(shopName: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val shop = db.shopDao().getShopByName(shopName)
            withContext(Dispatchers.Main) {
                if (shop != null) {
                    val intent = Intent(this@MainActivity, shop_shop::class.java).apply {
                        putExtra("shop_name", shop.name)
                        putExtra("shop_address", shop.address)
                        putExtra("shop_feeling", shop.feeling)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "データが見つかりませんでした。",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

