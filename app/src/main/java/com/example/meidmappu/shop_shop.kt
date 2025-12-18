package com.example.meidmappu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class shop_shop : AppCompatActivity() {

    private lateinit var storeId: String
    private lateinit var reviewContainer: LinearLayout
    private val firestore = Firebase.firestore

//    // 投稿画面から戻ったらレビュー再読み込み
//    private val reviewLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            // 投稿したら再読み込み
//            if (it.resultCode == RESULT_OK) {
//                loadReviews()
//            }
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_shop)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val paddingInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.ime()
            )
            v.setPadding(
                paddingInsets.left,
                paddingInsets.top,
                paddingInsets.right,
                paddingInsets.bottom
            )
            insets
        }

        // お店情報取得
        val name = intent.getStringExtra("shopName")
        val address = intent.getStringExtra("shopAddress")
        val feeling = intent.getStringExtra("feeling")
        val janruname = intent.getStringExtra("shopType")
        val concept = intent.getStringExtra("concept")
        val time = intent.getStringExtra("time")
        val image1Url = intent.getStringExtra("image")
        val image2Url = intent.getStringExtra("menu")
        storeId = intent.getStringExtra("shopId") ?: run {
            Toast.makeText(this, "お店情報が取得できません", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // UI部品
        val backBtn = findViewById<ImageButton>(R.id.backbtn)
        val nameText: TextView = findViewById(R.id.shop_name)
        val addressText: TextView = findViewById(R.id.shop_address)
        val reviewButton: Button = findViewById(R.id.review_button)
        val janruText: TextView = findViewById(R.id.janru_name)
        val conceptText: TextView = findViewById(R.id.concept_name)
        val timeText: TextView = findViewById(R.id.time_value)
        val feelingText: TextView = findViewById(R.id.feeling_value)
        val imageView: ImageView = findViewById(R.id.imageView4)
        val menuView: ImageView = findViewById(R.id.imageView5)
        reviewContainer = findViewById(R.id.review_container)

        // 表示セット
        nameText.text = name ?: ""
        addressText.text = address ?: ""
        janruText.text = janruname ?: ""
        conceptText.text = concept ?: ""
        timeText.text = time ?: ""
        feelingText.text = feeling ?: ""

        Glide.with(this).load(image1Url).placeholder(R.drawable.noimage).into(imageView)
        Glide.with(this).load(image2Url).placeholder(R.drawable.noimage).into(menuView)

        // Googleマップ
        if (!address.isNullOrEmpty()) {
            addressText.setOnClickListener {
                val mapUri = "geo:0,0?q=${Uri.encode(address)}".toUri()
                val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                startActivity(mapIntent)
            }
        }

        backBtn.setOnClickListener { finish() }

        // レビュー表示
        loadReviews()

        // 投稿ボタン
        reviewButton.setOnClickListener {
            val intent = Intent(this, ReviewPostActivity::class.java)
            intent.putExtra("shopId", storeId)
            startActivity(intent)
        }
    }

    // レビュー読み込み
    private fun loadReviews() {
        reviewContainer.removeAllViews()

        firestore.collection("shop")
            .document(storeId)
            .collection("reviews")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val rating = document.getLong("rating")?.toInt() ?: 0
                    val comment = document.getString("comment") ?: ""
                    val textView = TextView(this)
                    textView.text = "★$rating  $comment"
                    textView.textSize = 16f
                    reviewContainer.addView(textView)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "レビューの読み込みに失敗しました",
                    Toast.LENGTH_SHORT).show()
            }
    }
}
