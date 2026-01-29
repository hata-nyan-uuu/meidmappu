package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.floatingactionbutton.FloatingActionButton


class KodawariSearchResult : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopAdapter
    private lateinit var emptyText: TextView

    private val shopList = mutableListOf<ShopFirestore>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kodawari_search_result)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        recyclerView = findViewById(R.id.shopRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        emptyText = findViewById(R.id.emptyText)

        val back01 = findViewById<ImageButton>(R.id.backbtn3)
        back01.setOnClickListener { finish() }

        val type = intent.getStringExtra("type")
        val priceRange = intent.getLongExtra("priceRange", -1L).takeIf { it >= 0 }
        val concept = intent.getStringExtra("concept")
        val feeling = intent.getStringExtra("feeling")

        loadFilteredShops(type, priceRange, concept, feeling)

        val scrollTopBtn = findViewById<FloatingActionButton>(R.id.scrollTopBtn)

// RecyclerView のスクロールに応じて表示/非表示
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                if (rv.computeVerticalScrollOffset() > 300) {
                    scrollTopBtn.show()
                } else {
                    scrollTopBtn.hide()
                }
            }
        })

// ボタンを押したらトップに戻る
        scrollTopBtn.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

    }

    private fun loadFilteredShops(
        type: String?,
        priceRange: Long?,
        concept: String?,
        feeling: String?
    ) {
        val allShops = ShopRepository.getAll()
        shopList.clear()

        for (s in allShops) {
            val matchesType = type.isNullOrEmpty() || s.type == type
            val matchesPrice = priceRange == null || s.priceRange <= priceRange
            val matchesConcept = concept.isNullOrEmpty() || s.concept == concept
            val matchesFeeling =
                feeling.isNullOrEmpty() || s.feeling.contains(feeling)

            if (matchesType && matchesPrice && matchesConcept && matchesFeeling) {
                shopList.add(s)
            }
        }
        shopList.sortBy { it.timestamp }

        if (shopList.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            adapter = ShopAdapter(shopList) { shop ->
                val intent = Intent(this, shop_shop::class.java).apply {
                    putExtra("shopId", shop.id)
                    putExtra("FROM", "SEARCH")
                }
                startActivity(intent)
            }
            recyclerView.adapter = adapter
        }

        findViewById<ImageButton>(R.id.homeback1).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
