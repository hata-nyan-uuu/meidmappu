package com.example.meidmappu

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class all_shop : AppCompatActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_shop)

        val shopListText = findViewById<TextView>(R.id.textShop)
        db = AppDatabase.getDatabase(this)

        // データを取得して表示（読み取り専用）
        GlobalScope.launch(Dispatchers.IO) {
            val shops = db.shopDao().getAll()

            withContext(Dispatchers.Main) {
                if (shops.isEmpty()) {
                    shopListText.text = "現在、登録されているお店はありません。"
                } else {
                    val displayText = buildString {
                        shops.forEach {
                            append("店名：${it.name}\n住所：${it.address}\n説明：${it.description}\n\n")
                        }
                    }
                    shopListText.text = displayText
                }
            }
        }
    }
}
