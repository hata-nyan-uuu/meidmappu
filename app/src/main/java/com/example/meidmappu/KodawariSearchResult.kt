package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
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

        //前の画面にもどる
        val back01 = findViewById<ImageButton>(R.id.backbtn3)
        back01.setOnClickListener { finish() }

        val type = intent.getStringExtra("type")
        val maxPrice = intent.getIntExtra("maxPrice", -1).takeIf { it >= 0 }
        val concept = intent.getStringExtra("concept")
        val feeling = intent.getStringExtra("feeling")

        Log.d("DEBUG", "受け取った検索条件: type=$type maxPrice=$maxPrice concept=$concept feeling=$feeling")

        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val results = db.shopDao().filterShops(type, maxPrice, concept, feeling) // ← 同じく変更！

            // DB全件確認用ログ
            val allShops = db.shopDao().getAll()
            Log.d("DEBUG", "DB全件: ${allShops.size} 件")
            allShops.forEach { Log.d("DEBUG", it.toString()) }

            Log.d("DEBUG", "検索結果件数: ${results.size}")

            runOnUiThread {
                adapter = ShopAdapter(results) { shop ->

                    //値の渡しコード
                    val intent = Intent(this@KodawariSearchResult, shop_shop::class.java)
                    intent.putExtra("shopName", shop.name)
                    intent.putExtra("shopAddress", shop.address)
                    intent.putExtra("image1", shop.image ?: 0)
                    intent.putExtra("image2", shop.image2 ?: 0)
                    intent.putExtra("store_id", shop.id)
                    startActivity(intent)
                }
                recyclerView.adapter = adapter
            }
        }
    }
}
