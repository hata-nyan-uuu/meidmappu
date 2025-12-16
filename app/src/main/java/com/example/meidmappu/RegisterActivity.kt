package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // DB
        val db = UserDatabaseHelper(this)

        // XMLと対応したView取得
        val emailEdit = findViewById<EditText>(R.id.registerEmail)
        val passwordEdit = findViewById<EditText>(R.id.registerPassword)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val backButton = findViewById<Button>(R.id.registerBack)

        // 登録ボタン
        registerButton.setOnClickListener {

            val email = emailEdit.text.toString().trim()
            val password = passwordEdit.text.toString().trim()

            // --- 未入力チェック ---
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "メールアドレスとパスワードを入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- メールアドレス形式チェック（重要） ---
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "正しいメールアドレスを入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- パスワード最低文字数 ---
            if (password.length < 6) {
                Toast.makeText(this, "パスワードは6文字以上にしてください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- DB登録 ---
            val success = db.registerUser(email, password)

            if (success) {
                Toast.makeText(this, "登録が完了しました。ログインしてください", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "このメールアドレスは既に登録されています", Toast.LENGTH_SHORT).show()
            }
        }

        // 戻るボタン
        backButton.setOnClickListener {
            finish()
        }
    }
}
