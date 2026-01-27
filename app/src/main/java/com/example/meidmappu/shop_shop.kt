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

    private lateinit var randomButton: ImageButton
    private lateinit var homeBackButton: ImageButton

    // SNS
    private lateinit var linkSite: TextView
    private lateinit var linkX: TextView
    private lateinit var linkInstagram: TextView
    private lateinit var linkTiktok: TextView
    private lateinit var socialLinks: LinearLayout

    private val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_shop)

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

        // ===== View =====
        val backBtn = findViewById<ImageButton>(R.id.backbtn)
        randomButton = findViewById(R.id.randombtn)
        homeBackButton = findViewById(R.id.homeback2)

        val nameText = findViewById<TextView>(R.id.shop_name)
        val addressText = findViewById<TextView>(R.id.shop_address)
        val janruText = findViewById<TextView>(R.id.janru_name)
        val conceptText = findViewById<TextView>(R.id.concept_name)
        val timeText = findViewById<TextView>(R.id.time_value)
        val feelingText = findViewById<TextView>(R.id.feeling_value)
        val imageView = findViewById<ImageView>(R.id.imageView4)
        val menuView = findViewById<ImageView>(R.id.imageView5)
        val reviewButton = findViewById<Button>(R.id.review_button)
        val priceValue = findViewById<TextView>(R.id.price_value)


        reviewContainer = findViewById(R.id.review_container)

        // SNS
        linkSite = findViewById(R.id.link_site)
        linkX = findViewById(R.id.link_x)
        linkInstagram = findViewById(R.id.link_instagram)
        linkTiktok = findViewById(R.id.link_tiktok)
        socialLinks = findViewById(R.id.social_links)

        // ===== Intent =====
        storeId = intent.getStringExtra("shopId") ?: run {
            Toast.makeText(this, "お店情報が取得できません", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val from = intent.getStringExtra("FROM")

        // ===== ボタン初期状態 =====
        randomButton.visibility = View.GONE
        homeBackButton.visibility = View.GONE

        when (from) {
            "RANDOM" -> randomButton.visibility = View.VISIBLE
            "SEARCH" -> homeBackButton.visibility = View.VISIBLE
            else -> {

            }
        }

        // ===== 戻る =====
        backBtn.setOnClickListener { finish() }

        // ===== ホーム =====
        homeBackButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // ===== ランダム =====
        randomButton.setOnClickListener {
            val shops = ShopRepository.getAll()

            if (shops.isNotEmpty()) {
                val randomShop = shops.random()

                startActivity(
                    Intent(this, shop_shop::class.java).apply {
                        putExtra("shopId", randomShop.id)
                        putExtra("FROM", "RANDOM")
                    }
                )
                finish()
            } else {
                Toast.makeText(this, "お店データがありません", Toast.LENGTH_SHORT).show()
            }
        }

        // ===== レビュー投稿 =====
        reviewButton.setOnClickListener {
            startActivity(
                Intent(this, ReviewPostActivity::class.java)
                    .putExtra("shopId", storeId)
            )
        }

        // ===== 店情報取得 =====
        val shop = ShopRepository.getById(storeId)
            ?: run {
                Toast.makeText(this, "お店情報が取得できません", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

        nameText.text = shop.name ?: ""
        addressText.text = shop.address ?: ""
        janruText.text = shop.type ?: ""
        conceptText.text = shop.concept ?: ""
        timeText.text = shop.time ?: ""

        feelingText.text = shop.feeling
            ?.joinToString("\n") { "#$it" }
            ?: ""

        Glide.with(this)
            .load(shop.image)
            .placeholder(R.drawable.noimage)
            .into(imageView)

        Glide.with(this)
            .load(shop.menu)
            .placeholder(R.drawable.noimage)
            .into(menuView)

// 地図
        if (!shop.address.isNullOrBlank()) {
            addressText.setOnClickListener {
                val uri = "geo:0,0?q=${Uri.encode(shop.address)}".toUri()
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }

// SNS
        setupLink(linkSite, shop.website)
        setupLink(linkX, shop.x)
        setupLink(linkInstagram, shop.instagram)
        setupLink(linkTiktok, shop.tiktok)

// まとめて非表示
        socialLinks.visibility =
            if (shop.website.isNullOrBlank()
                && shop.x.isNullOrBlank()
                && shop.instagram.isNullOrBlank()
                && shop.tiktok.isNullOrBlank()
            ) View.GONE else View.VISIBLE

// 価格
        priceValue.text =
            shop.priceRange?.let { "～${String.format("%,d", it)}円" } ?: "未設定"

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
                    tv.text = getString(R.string.review_text,
                        rating, comment)
                    tv.textSize = 16f
                    reviewContainer.addView(tv)
                }
            }
    }

    // ===== SNSリンク =====
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
