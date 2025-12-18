package com.example.meidmappu

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReviewPostActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var shopId: String
    private lateinit var ratingBar: RatingBar
    private lateinit var commentEditText: EditText
    private lateinit var postButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_post)


        firestore = Firebase.firestore
        shopId = intent.getStringExtra("shopId") ?: run {
            Toast.makeText(this, "お店情報が取得できません",
                Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        ratingBar = findViewById(R.id.reviewRatingBar)
        commentEditText = findViewById(R.id.reviewComment)
        postButton = findViewById(R.id.buttonReviewSubmit)
        backButton = findViewById(R.id.buttonReviewBack)

        // 投稿処理
        postButton.setOnClickListener {
            val rating = ratingBar.rating.toInt()
            val comment = commentEditText.text.toString()
            if (comment.isBlank()) {
                Toast.makeText(this, "コメントを入力してください",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            postReview(rating, comment)
        }

        backButton.setOnClickListener { finish() }
    }


    private fun postReview(rating: Int, comment: String) {
        val review = hashMapOf(
            "userId" to "", // 後でログイン機能を追加する場合に Firebase UID
            "rating" to rating,
            "comment" to comment,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("shop")
            .document(shopId)
            .collection("reviews")
            .add(review)
            .addOnSuccessListener {
                Toast.makeText(this, "レビューを投稿しました", Toast.LENGTH_SHORT).show()
                finish() // shop_shop に戻る
            }
            .addOnFailureListener {
                Toast.makeText(this, "投稿に失敗しました", Toast.LENGTH_SHORT).show()
            }
    }
}
