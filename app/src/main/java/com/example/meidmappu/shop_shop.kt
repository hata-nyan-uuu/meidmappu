package com.example.meidmappu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class shop_shop : AppCompatActivity() {

    private var storeId: Int = -1
    private lateinit var reviewContainer: LinearLayout
    private lateinit var db: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_shop)

        // DB
        db = UserDatabaseHelper(this)

        // ====== ① お店データを受け取る ======
        val name = intent.getStringExtra("shopName")
        val address = intent.getStringExtra("shopAddress")
        val image1Url = intent.getStringExtra("image")
        val image2Url = intent.getStringExtra("menu")
        val type = intent.getStringExtra("shopType")

        storeId = intent.getIntExtra("store_id", -1)

        // ====== ② UI 部品 ======
        val back01 = findViewById<ImageButton>(R.id.backbtn)
        back01.setOnClickListener { finish() }

        val nameText: TextView = findViewById(R.id.shop_name)
        val addressText: TextView = findViewById(R.id.shop_address)
        val reviewButton: Button = findViewById(R.id.review_button)
        val image: ImageView = findViewById(R.id.imageView4)
        val menu: ImageView = findViewById(R.id.imageView5)
        val typeText: TextView = findViewById(R.id.textshoptype)

        reviewContainer = findViewById(R.id.review_container)
        typeText.text = type ?: ""

        // ====== ③ お店情報の表示 ======
        nameText.text = name ?: ""
        addressText.text = address ?: ""

        // URL → Glide で画像を表示
        Glide.with(this)
            .load(image1Url)
            .placeholder(R.drawable.noimage)
            .into(image)

        Glide.with(this)
            .load(image2Url)
            .placeholder(R.drawable.noimage)
            .into(menu)

        // --- 住所 → Googleマップを開く ---
        if (!address.isNullOrEmpty()) {
            addressText.setOnClickListener {
                val mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address))
                val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                startActivity(mapIntent)
            }
        }

        // ====== ④ レビュー一覧の表示 ======
        if (storeId != -1) {
            loadReviews(storeId)
        }

        // ====== ⑤ レビュー投稿ボタン ======
        reviewButton.setOnClickListener {
            val intent = Intent(this, ReviewPostActivity::class.java)
            intent.putExtra("store_id", storeId)
            startActivity(intent)
        }
    }

    // ====== レビュー読み込み ======
    private fun loadReviews(storeId: Int) {
        reviewContainer.removeAllViews()

        val reviews = db.getReviewsByStoreId(storeId)

        for (review in reviews) {
            val textView = TextView(this)
            textView.text = "★${review.rating}  ${review.comment}"
            textView.textSize = 16f
            reviewContainer.addView(textView)
        }
    }
}
