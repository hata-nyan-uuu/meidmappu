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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RandomKensaku : AppCompatActivity() {

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

        // 戻るボタン
        findViewById<ImageButton>(R.id.backbtn1).setOnClickListener { finish() }

        // ランダム取得ボタン
        findViewById<ImageButton>(R.id.randomStart).setOnClickListener {
            lifecycleScope.launch {
                fetchRandomShop()
            }
        }
    }

    private fun fetchRandomShop() {
        val shops = ShopRepository.getAll()

        if (shops.isEmpty()) return

        val randomShop = shops.random()

        val intent = Intent(this, shop_shop::class.java).apply {
            putExtra("shopId", randomShop.id)
            putExtra("FROM", "RANDOM")
        }
        startActivity(intent)
    }
}