package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FavoritesShopActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val favoriteShops = mutableListOf<ShopFirestore>()
    private lateinit var adapter: ShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorites_shop)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 戻る
        findViewById<ImageButton>(R.id.backbtn).setOnClickListener {
            finish()
        }

        //ホームボタン
        findViewById<ImageButton>(R.id.homeback).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
            startActivity(intent)
        }


        val recyclerView = findViewById<RecyclerView>(R.id.favoriteRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ShopAdapter(favoriteShops) { shop ->
            startActivity(
                Intent(this, shop_shop::class.java)
                    .putExtra("shopId", shop.id)
            )
        }
        recyclerView.adapter = adapter

        // トップに戻るボタン
        val scrollTopBtn = findViewById<FloatingActionButton>(R.id.scrollTopBtn)

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

        scrollTopBtn.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }

        loadFavorites()
    }

    private fun loadFavorites() {
        val user = auth.currentUser ?: return

        db.collection("users")
            .document(user.uid)
            .collection("favorites")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                favoriteShops.clear()

                for (doc in result) {
                    favoriteShops.add(
                        ShopFirestore(
                            id = doc.getString("shopId") ?: "",
                            name = doc.getString("name") ?: "",
                            image = doc.getString("image")
                        )
                    )
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "お気に入り取得失敗", Toast.LENGTH_SHORT).show()
            }


    }
}
