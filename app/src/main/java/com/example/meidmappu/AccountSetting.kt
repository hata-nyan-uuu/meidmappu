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
        val passwordButton = findViewById<ImageButton>(R.id.PWchenge)
        val logoutButton = findViewById<ImageButton>(R.id.logout)

        // 戻るボタン
        backButton.setOnClickListener {
            finish() // 1つ前の画面に戻る
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
    }
}
