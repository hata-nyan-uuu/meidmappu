package com.example.meidmappu
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.card.MaterialCardView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var adapter: ShopAdapter2
    private val shopList = mutableListOf<Shop>()   // DBの店舗一覧を保持

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // インセット調整
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // DB
        db = AppDatabase.getDatabase(this)

        // RecyclerView 初期化
        val recyclerView = findViewById<RecyclerView>(R.id.homeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ShopAdapter2(shopList) { shop ->
            openShopDetail(shop)
        }
        recyclerView.adapter = adapter

        // DBから全店舗読み込み
        loadAllShops()

        // ボタン設定
        setButtonListeners()
    }

    /** DBから全店舗を取得してRecyclerViewに表示 */
    private fun loadAllShops() {
        lifecycleScope.launch(Dispatchers.IO) {
            val list = db.shopDao().getAll()   // ← DAOに必要

            withContext(Dispatchers.Main) {
                shopList.clear()
                shopList.addAll(list)
                adapter.notifyDataSetChanged()
            }
        }
    }

    /** 詳細画面を開く */
    private fun openShopDetail(shop: Shop) {
        val intent = Intent(this, shop_shop::class.java).apply {
            putExtra("shopName", shop.name)
            // Int? (Null許容) を Int に安全に変換 (Nullなら0)
            putExtra("image1", shop.image ?: 0)
            putExtra("image2", shop.image2 ?: 0)
            putExtra("shopAddress", shop.address)
            putExtra("store_id", shop.id)
        }
        startActivity(intent)
    }

    /** ボタン設定 */
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
        findViewById<Button>(R.id.settingbtn).setOnClickListener {
            startActivity(Intent(this, setting::class.java))
        }
    }
}
