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
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.AlertDialog


class shop_shop : AppCompatActivity() {

    private fun showLoginRequiredDialog() {
        AlertDialog.Builder(this)
            .setTitle("ログインが必要です")
            .setMessage("この機能を利用するにはログインが必要です。")
            .setPositiveButton("ログイン") { _, _ ->
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    putExtra("RETURN_TO","SHOP")
                    putExtra("shopId",storeId)
                })
            }
            .setNegativeButton("キャンセル", null)
            .show()
    }


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
    private fun normalizeTilde(text: String?): String {
        return text
            ?.replace("〜", "～") // 波ダッシュ → 全角チルダ
            ?.replace("~","～") //半角から全角チルダ
            ?: ""
    }
    private lateinit var favoriteButton: ImageButton
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()




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
        homeBackButton = findViewById(R.id.homeback)

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
        backBtn.visibility= View.GONE

        when (from) {
            "RANDOM" ->{
                randomButton.visibility = View.VISIBLE
                homeBackButton.visibility= View.VISIBLE
                backBtn.visibility= View.GONE
                }
            else -> {
                randomButton.visibility = View.GONE
                homeBackButton.visibility = View.VISIBLE
                backBtn.visibility=View.VISIBLE
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
            if (auth.currentUser == null) {
                showLoginRequiredDialog()
                return@setOnClickListener
            }

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
        // ===== お気に入り =====
        favoriteButton = findViewById(R.id.favoriteButton)

        val user = auth.currentUser
        if (user == null) {
            favoriteButton.setImageResource(R.drawable.heart_off)
            favoriteButton.setOnClickListener {
                showLoginRequiredDialog()
            }
        } else {

            val favoriteRef = db.collection("users")
                .document(user.uid)
                .collection("favorites")
                .document(storeId)

            // 初期状態
            favoriteRef.get().addOnSuccessListener { doc ->
                if (doc.exists()) {
                    favoriteButton.setImageResource(R.drawable.heart_on)
                } else {
                    favoriteButton.setImageResource(R.drawable.heart_off)
                }
            }

            // クリック
            favoriteButton.setOnClickListener {
                favoriteRef.get().addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        favoriteRef.delete()
                        favoriteButton.setImageResource(R.drawable.heart_off)
                        Toast.makeText(this, "お気に入り解除", Toast.LENGTH_SHORT).show()
                    } else {
                        val data = hashMapOf(
                            "shopId" to storeId,
                            "name" to shop.name,
                            "image" to shop.image,
                            "timestamp" to System.currentTimeMillis()
                        )
                        favoriteRef.set(data)
                        favoriteButton.setImageResource(R.drawable.heart_on)
                        Toast.makeText(this, "お気に入り追加", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



        nameText.text = normalizeTilde(shop.name)
        addressText.text = normalizeTilde(shop.address)
        janruText.text = normalizeTilde(shop.type)
        conceptText.text = normalizeTilde(shop.concept)
        timeText.text = normalizeTilde(shop.time)

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
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Toast.makeText(this, "レビューの読み込みに失敗しました", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                reviewContainer.removeAllViews() // 毎回更新
                snapshots?.forEach { doc ->
                    val name = doc.getString("name") ?: "名無し"
                    val rating = doc.getLong("rating")?.toInt() ?: 0
                    val comment = doc.getString("comment") ?: ""

                    val tv = TextView(this)
                    tv.text = "$name: ★$rating\n$comment"
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
