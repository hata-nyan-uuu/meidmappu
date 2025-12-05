package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ReviewPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_post)

        val db = UserDatabaseHelper(this)

        // --- 店IDを受け取る ---
        val storeId = intent.getIntExtra("store_id", -1)
        if (storeId == -1) {
            Toast.makeText(this, "お店情報が取得できません", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // --- ログインユーザーID ---
        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val userId = prefs.getInt("login_user_id", -1)

        if (userId == -1) {
            Toast.makeText(this, "ログインが必要です", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // --- UI ---
        val ratingBar = findViewById<RatingBar>(R.id.reviewRatingBar)
        val commentEdit = findViewById<EditText>(R.id.reviewComment)
        val submitButton = findViewById<Button>(R.id.buttonReviewSubmit)
        val backButton = findViewById<Button>(R.id.buttonReviewBack)

        // --- 投稿処理 ---
        submitButton.setOnClickListener {

            val rating = ratingBar.rating.toInt()
            val comment = commentEdit.text.toString()

            if (comment.isEmpty()) {
                Toast.makeText(this, "コメントを入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = db.insertReview(
                userId = userId,
                storeId = storeId,
                rating = rating,
                comment = comment
            )

            if (success) {
                Toast.makeText(this, "レビューを投稿しました", Toast.LENGTH_SHORT).show()

                // --- reviews 更新フラグを返す ---
                val resultIntent = Intent()
                resultIntent.putExtra("review_updated", true)
                setResult(RESULT_OK, resultIntent)

                finish()
            } else {
                Toast.makeText(this, "投稿に失敗しました", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}