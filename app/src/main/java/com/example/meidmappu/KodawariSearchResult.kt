package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KodawariSearchResult : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kodawari_search_result)

        recyclerView = findViewById(R.id.shopRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 検索条件を Intent から取得
        val type = intent.getStringExtra("type")
        val maxPrice = intent.getIntExtra("maxPrice", -1).takeIf { it >= 0 }
        val concept = intent.getStringExtra("concept")
        val menu = intent.getStringExtra("menu")

        // DBから結果を取得（Room）→ Coroutine
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val results = db.shopDao().filterShops(type, maxPrice, concept, menu)

            // RecyclerView更新はUIスレッドで
            runOnUiThread {
                adapter = ShopAdapter(results) { shop ->
                    // クリック時に詳細画面へ
                    val intent = Intent(this@KodawariSearchResult, shop_shop::class.java)
                    intent.putExtra("shopId", shop.id)
                    startActivity(intent)
                }
                recyclerView.adapter = adapter
            }
        }
    }
}
