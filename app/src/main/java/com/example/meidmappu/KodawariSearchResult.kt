package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore



class KodawariSearchResult : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopFirestoreAdapter
    private val shopList = mutableListOf<ShopFirestore>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kodawari_search_result)

        recyclerView = findViewById(R.id.shopRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val back01 = findViewById<ImageButton>(R.id.backbtn3)
        back01.setOnClickListener { finish() }

        val type = intent.getStringExtra("type")
        val priceRange = intent.getLongExtra("priceRange", -1L)
            .takeIf { it >= 0 }
        val concept = intent.getStringExtra("concept")
        val feeling = intent.getStringExtra("feeling")

        loadFilteredShops(type, priceRange, concept, feeling)
    }

    private fun loadFilteredShops(
        type: String?,
        priceRange: Long?,
        concept: String?,
        feeling: String?
    ) {
        db.collection("shop")
            .get()
            .addOnSuccessListener { result ->
                shopList.clear()
                for (document in result) {
                    val shop = document.toObject(ShopFirestore::class.java)
                    // Kotlin 側で条件フィルタ
                    val matchesType = type.isNullOrEmpty() || shop.type == type
                    val matchesPrice = priceRange == null || shop.priceRange == priceRange.toLong()
                    val matchesConcept = concept.isNullOrEmpty() || shop.concept == concept
                    val matchesFeeling = feeling.isNullOrEmpty() || shop.feeling == feeling

                    if (matchesType && matchesPrice && matchesConcept && matchesFeeling) {
                        shopList.add(shop)
                    }

                }

                adapter = ShopFirestoreAdapter(shopList) { shop ->
                    val intent = Intent(this, shop_shop::class.java).apply {
                        putExtra("shopName", shop.name)
                        putExtra("image", shop.image)       // 店舗画像URL
                        putExtra("menu", shop.menu)         // メニュー画像URL
                        putExtra("shopAddress", shop.address)
                        putExtra("shopType", shop.type)     // お店タイプ
                        putExtra("feeling", shop.feeling)   // 雰囲気
                        putExtra("concept", shop.concept)   // コンセプト
                        putExtra("priceRange", shop.priceRange)
                        putExtra("time", shop.time)
                    }
                    startActivity(intent)
                }
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "データ取得失敗", e)
            }
    }
}
