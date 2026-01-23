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
                // 戻るボタンのみ
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
                        startActivity(
                            Intent(this, shop_shop::class.java).apply {
                                putExtra("shopId", randomShop.id)
                                putExtra("FROM", "RANDOM")
                            }
                        )
                        finish()
                    }
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
        firestore.collection("shop")
            .document(storeId)
            .get()
            .addOnSuccessListener { doc ->
                nameText.text = doc.getString("name") ?: ""
                addressText.text = doc.getString("address") ?: ""
                janruText.text = doc.getString("type") ?: ""
                conceptText.text = doc.getString("concept") ?: ""
                timeText.text = doc.getString("time") ?: ""
                val feelings = doc.get("feeling") as? List<*>

                feelingText.text = feelings
                    ?.filterIsInstance<String>()
                    ?.joinToString("\n") { "#$it" }
                    ?: ""

                Glide.with(this)
                    .load(doc.getString("image"))
                    .placeholder(R.drawable.noimage)
                    .into(imageView)

                Glide.with(this)
                    .load(doc.getString("menu"))
                    .placeholder(R.drawable.noimage)
                    .into(menuView)

                if (!doc.getString("address").isNullOrBlank()) {
                    addressText.setOnClickListener {
                        val uri =
                            "geo:0,0?q=${Uri.encode(doc.getString("address"))}".toUri()
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                }

                // ===== SNS =====
                val website = doc.getString("website")
                val x = doc.getString("x")
                val instagram = doc.getString("instagram")
                val tiktok = doc.getString("tiktok")

                setupLink(linkSite, website)
                setupLink(linkX, x)
                setupLink(linkInstagram, instagram)
                setupLink(linkTiktok, tiktok)

                // 全部空ならまとめて非表示
                if (website.isNullOrBlank()
                    && x.isNullOrBlank()
                    && instagram.isNullOrBlank()
                    && tiktok.isNullOrBlank()
                ) {
                    socialLinks.visibility = View.GONE
                } else {
                    socialLinks.visibility = View.VISIBLE
                }
                //priceRange
                val price = doc.getLong("priceRange")

                priceValue.text = if (price != null) {
                    "～${String.format("%,d", price)}円"
                } else {
                    "未設定"
                }
            }

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
