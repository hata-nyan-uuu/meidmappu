package com.example.meidmappu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class shop_shop : AppCompatActivity() {

    private lateinit var storeId: String
    private lateinit var reviewContainer: LinearLayout

    private lateinit var linkSite: TextView
    private lateinit var linkX: TextView
    private lateinit var linkInstagram: TextView
    private lateinit var linkTiktok: TextView
    private lateinit var socialLinks: LinearLayout

    private lateinit var randomButton: ImageButton

    private val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ===== Edge to Edge =====
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_shop)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ===== Intent（shopIdのみ + ランダムフラグ） =====
        storeId = intent.getStringExtra("shopId") ?: run {
            Toast.makeText(this, "お店情報が取得できません", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val fromRandom = intent.getBooleanExtra("FROM_RANDOM", false)

        // ===== View =====
        val backBtn = findViewById<ImageButton>(R.id.backbtn)
        val nameText = findViewById<TextView>(R.id.shop_name)
        val addressText = findViewById<TextView>(R.id.shop_address)
        val janruText = findViewById<TextView>(R.id.janru_name)
        val conceptText = findViewById<TextView>(R.id.concept_name)
        val timeText = findViewById<TextView>(R.id.time_value)
        val feelingText = findViewById<TextView>(R.id.feeling_value)
        val imageView = findViewById<ImageView>(R.id.imageView4)
        val menuView = findViewById<ImageView>(R.id.imageView5)
        val reviewButton = findViewById<Button>(R.id.review_button)
        randomButton = findViewById(R.id.randombtn)

        reviewContainer = findViewById(R.id.review_container)

        linkSite = findViewById(R.id.link_site)
        linkX = findViewById(R.id.link_x)
        linkInstagram = findViewById(R.id.link_instagram)
        linkTiktok = findViewById(R.id.link_tiktok)
        socialLinks = findViewById(R.id.social_links)

        // ===== 戻るボタン =====
        backBtn.setOnClickListener { finish() }

        // ===== レビュー投稿ボタン =====
        reviewButton.setOnClickListener {
            startActivity(Intent(this, ReviewPostActivity::class.java).putExtra("shopId", storeId))
        }

        // ===== ランダムボタン表示制御 =====
        if (fromRandom) {
            randomButton.visibility = View.VISIBLE
            randomButton.setOnClickListener {
                // Firestoreから全店舗取得してランダムで1件選ぶ
                firestore.collection("shop")
                    .get()
                    .addOnSuccessListener { result ->
                        val shops = result.documents.mapNotNull { doc ->
                            val shop = doc.toObject(ShopFirestore::class.java)
                            shop?.id = doc.id
                            shop
                        }
                        if (shops.isNotEmpty()) {
                            val randomShop = shops.random()
                            startActivity(Intent(this, shop_shop::class.java).apply {
                                putExtra("shopId", randomShop.id)
                                putExtra("FROM_RANDOM", true)
                            })
                            finish() // 今の画面を閉じて切り替え
                        }
                    }
            }
        } else {
            randomButton.visibility = View.GONE
        }

        // ===== 店情報取得 =====
        firestore.collection("shop")
            .document(storeId)
            .get()
            .addOnSuccessListener { doc ->

                val name = doc.getString("name")
                val address = doc.getString("address")
                val type = doc.getString("type")
                val concept = doc.getString("concept")
                val time = doc.getString("time")
                val feeling = doc.getString("feeling")
                val image = doc.getString("image")
                val menu = doc.getString("menu")

                nameText.text = name ?: ""
                addressText.text = address ?: ""
                janruText.text = type ?: ""
                conceptText.text = concept ?: ""
                timeText.text = time ?: ""
                feelingText.text = feeling ?: ""

                Glide.with(this).load(image).placeholder(R.drawable.noimage).into(imageView)
                Glide.with(this).load(menu).placeholder(R.drawable.noimage).into(menuView)

                // 住所クリックでマップ
                if (!address.isNullOrBlank()) {
                    addressText.setOnClickListener {
                        val uri = "geo:0,0?q=${Uri.encode(address)}".toUri()
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                }

                // SNS / 公式リンク
                setupLink(linkSite, doc.getString("website"))
                setupLink(linkX, doc.getString("x"))
                setupLink(linkInstagram, doc.getString("instagram"))
                setupLink(linkTiktok, doc.getString("tiktok"))

                if (doc.getString("website").isNullOrBlank() &&
                    doc.getString("x").isNullOrBlank() &&
                    doc.getString("instagram").isNullOrBlank() &&
                    doc.getString("tiktok").isNullOrBlank()
                ) {
                    socialLinks.visibility = View.GONE
                }
            }

        // ===== レビュー読み込み =====
        loadReviews()
    }

    // ===== レビュー =====
    private fun loadReviews() {
        reviewContainer.removeAllViews()

        firestore.collection("shop")
            .document(storeId)
            .collection("reviews")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    val rating = doc.getLong("rating")?.toInt() ?: 0
                    val comment = doc.getString("comment") ?: ""
                    val tv = TextView(this)
                    tv.text = "★$rating  $comment"
                    tv.textSize = 16f
                    reviewContainer.addView(tv)
                }
            }
    }

    // ===== 共通リンク処理 =====
    private fun setupLink(textView: TextView, url: String?) {
        if (url.isNullOrBlank()) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            textView.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        }
    }
}
