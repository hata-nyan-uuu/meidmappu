package com.example.meidmappu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RandomKensaku : AppCompatActivity() {

    private lateinit var db: AppDatabase

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_random_kensaku)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.getDatabase(this)

        //前の画面にもどる
        val back01 = findViewById<ImageButton>(R.id.imageButton11)
        back01.setOnClickListener { finish() }

        // 🔹 ランダム取得ボタン
        findViewById<ImageButton>(R.id.randomStart).setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val allShops = db.shopDao().getAllShops()
                if (allShops.isNotEmpty()) {
                    val randomShop = allShops.random()
                    withContext(Dispatchers.Main) {
                        val intent = Intent(this@RandomKensaku, shop_shop::class.java).apply {
                            putExtra("shop_name", randomShop.name)
                            putExtra("shop_address", randomShop.address)
                            putExtra("shop_feeling", randomShop.feeling)
                        }
                        startActivity(intent)
                    }
                }
            }
        }
    }
}