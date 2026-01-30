package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.UserProfileChangeRequest
import android.widget.TextView



class AccountSetting : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account_setting)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        // ボタン取得
        val backButton = findViewById<ImageButton>(R.id.backbtnsetting)
        val usernameText = findViewById<TextView>(R.id.username)
        val nameChangeButton = findViewById<Button>(R.id.namechange)
        val favoritesButton = findViewById<Button>(R.id.favorites)
//        val reviewButton = findViewById<Button>(R.id.user_review)
        val passwordButton = findViewById<Button>(R.id.PWchange)
        val logoutButton = findViewById<Button>(R.id.logout)
        val user = auth.currentUser
        usernameText.text = user?.displayName ?: "ユーザー名未設定"


        // 戻るボタン
        backButton.setOnClickListener {
            finish() // 1つ前の画面に戻る
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

        //パスワード変更（メール送信）
        passwordButton.setOnClickListener {
            val user = auth.currentUser

            if (user?.email != null) {
                auth.sendPasswordResetEmail(user.email!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "パスワード変更用メールを送信しました",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this,
                                "メール送信に失敗しました",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        //ログアウト
        logoutButton.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "ログアウトしました", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        //レビュー履歴

//        reviewButton.setOnClickListener {
//            val intent = Intent(this, ReviewHistoryActivity::class.java)
//            startActivity(intent)
//        }
        //お気に入り

        favoritesButton.setOnClickListener {
            val intent = Intent(this, FavoritesShopActivity::class.java)
            startActivity(intent)
        }
        //名前を変える
        // 名前変更
        nameChangeButton.setOnClickListener {
            val editText = EditText(this)
            editText.hint = "新しいユーザー名"

            AlertDialog.Builder(this)
                .setTitle("ユーザー名変更")
                .setView(editText)
                .setPositiveButton("変更") { _, _ ->
                    val newName = editText.text.toString().trim()
                    if (newName.isEmpty()) return@setPositiveButton

                    val user = auth.currentUser ?: return@setPositiveButton

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(newName)
                        .build()

                    user.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                usernameText.text = newName
                                Toast.makeText(this, "名前を変更しました", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                .setNegativeButton("キャンセル", null)
                .show()
        }

    }
}
