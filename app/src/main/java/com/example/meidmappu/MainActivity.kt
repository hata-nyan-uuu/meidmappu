package com.example.meidmappu

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import android.content.Intent
import android.os.Bundle
import com.google.android.material.card.MaterialCardView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ShopAdapter
    private val shopList = mutableListOf<ShopFirestore>()   // Firestore から取るデータ
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //AD
        MobileAds.initialize(this)

        val adView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


        // インセット調整
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // RecyclerView 初期化
        val recyclerView = findViewById<RecyclerView>(R.id.homeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ShopAdapter(shopList) { shop ->
            openShopDetail(shop)
        }
        recyclerView.adapter = adapter

        // Firestore から店舗データ読み込み
        loadAllShops()

        // ボタン設定
        setButtonListeners()
    }


    // Firestoreから全店舗を取得してRecyclerViewに表示
    private fun loadAllShops() {
        db.collection("shop")
            .orderBy("timestamp",
                com.google.firebase.firestore.Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                shopList.clear()

                for (document in result) {
                    val shop = document.toObject(ShopFirestore::class.java).apply {
                        id = document.id
                    }
                    shopList.add(shop)
                }

                //オフライン用に保存
                ShopRepository.setShops(shopList)

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "データ取得失敗: ", e)
            }
    }

    // 詳細画面を開く
    private fun openShopDetail(shop: ShopFirestore) {
        val intent = Intent(this, shop_shop::class.java).apply {
            putExtra("shopId", shop.id)
        }
        startActivity(intent)
    }

    // ボタン設定（Roomの時と同じ）
    private fun setButtonListeners() {
        findViewById<MaterialCardView>(R.id.random1).setOnClickListener {
            startActivity(Intent(this, RandomKensaku::class.java))
        }
        findViewById<MaterialCardView>(R.id.kodawari).setOnClickListener {
            startActivity(Intent(this, kodawari_kensaku::class.java))
        }
        findViewById<MaterialCardView>(R.id.hazimete).setOnClickListener {
            startActivity(Intent(this, tyutoriaru01::class.java))
        }
        findViewById<ImageButton>(R.id.settingbtn).setOnClickListener {
            startActivity(Intent(this, setting::class.java))
        }
    }
}
