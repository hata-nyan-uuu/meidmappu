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
                    val shop = document.toObject(ShopFirestore::class.java)?.apply {
                        id = document.id
                    }

                    shop?.let { s ->
                        val matchesType = type.isNullOrEmpty() || s.type == type
                        val matchesPrice = priceRange == null || s.priceRange == priceRange
                        val matchesConcept = concept.isNullOrEmpty() || s.concept == concept
                        val matchesFeeling = feeling.isNullOrEmpty() || s.feeling == feeling

                        if (matchesType && matchesPrice && matchesConcept && matchesFeeling) {
                            shopList.add(s)
                        }
                    }
                }

                adapter = ShopFirestoreAdapter(shopList) { shop ->
                    val intent = Intent(this, shop_shop::class.java).apply {
                        putExtra("shopName", shop.name)
                        putExtra("image", shop.image)
                        putExtra("menu", shop.menu)
                        putExtra("shopAddress", shop.address)
                        putExtra("shopType", shop.type)
                        putExtra("feeling", shop.feeling)
                        putExtra("concept", shop.concept)
                        putExtra("priceRange", shop.priceRange)
                        putExtra("time", shop.time)
                        putExtra("shopId", shop.id)
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
