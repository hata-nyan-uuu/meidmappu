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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.ConcatAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ShopAdapter
    private lateinit var headerAdapter: HeaderAdapter
    private val shopList = mutableListOf<ShopFirestore>()   // Firestore から取るデータ
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //AD
        MobileAds.initialize(this)
        val adView = findViewById<AdView>(R.id.adView)
        adView.loadAd(AdRequest.Builder().build())


        // インセット調整
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // RecyclerView 初期化
        val recyclerView = findViewById<RecyclerView>(R.id.homeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // 1. ヘッダーアダプターの初期化（ボタン処理をここへ移動）
        headerAdapter = HeaderAdapter { id ->
            when (id) {
                R.id.random1 -> startActivity(Intent(this, RandomKensaku::class.java))
                R.id.kodawari -> startActivity(Intent(this, kodawari_kensaku::class.java))
                R.id.hazimete -> startActivity(Intent(this, tyutoriaru01::class.java))
                R.id.settingbtn -> startActivity(Intent(this, setting::class.java))
            }
        }

        // 2. 店舗リスト
        adapter = ShopAdapter(shopList) { shop ->
            openShopDetail(shop)
        }

        // 3. 合体
        val concatAdapter = ConcatAdapter(headerAdapter, adapter)
        recyclerView.adapter = concatAdapter

        loadAllShops()
        setScrollButtons() // 新しいスクロール監視を開始
    }

    // スクロールボタン（FAB）の制御
    private fun setScrollButtons() {
        val recyclerView = findViewById<RecyclerView>(R.id.homeRecyclerView)
        val scrollTopBtn = findViewById<FloatingActionButton>(R.id.scrollTopBtn)

        // NestedScrollViewではなくRecyclerViewのスクロールを監視
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // 縦のスクロール量を取得
                val offset = recyclerView.computeVerticalScrollOffset()
                if (offset > 300) scrollTopBtn.show() else scrollTopBtn.hide()
            }
        })

        scrollTopBtn.setOnClickListener {
            recyclerView.smoothScrollToPosition(0) // 0番目（ヘッダー）まで戻る
        }
    }


    // Firestoreから全店舗を取得してRecyclerViewに表示
    private fun loadAllShops() {
        db.collection("shop")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                shopList.clear()
                for (document in result) {
                    val shop = document.toObject(ShopFirestore::class.java).apply {
                        id = document.id
                    }
                    shopList.add(shop) // 追加を忘れずに
                }
                ShopRepository.setShops(shopList)
                adapter.notifyDataSetChanged() // 変数名を adapter に修正
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Firestore失敗", e)
                val localShops = ShopRepository.getAll()
                if (localShops.isNotEmpty()) {
                    shopList.clear()
                    shopList.addAll(localShops)
                    adapter.notifyDataSetChanged()
                }
            }
    }
    // 詳細画面を開く
    private fun openShopDetail(shop: ShopFirestore) {
        val intent = Intent(this, shop_shop::class.java).apply {
            putExtra("shopId", shop.id)
        }
        startActivity(intent)
    }
}
