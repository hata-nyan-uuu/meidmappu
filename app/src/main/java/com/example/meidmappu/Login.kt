package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // ▼ Edge-to-edge の余白調整
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailEdit = findViewById<EditText>(R.id.editEmail)
        val passwordEdit = findViewById<EditText>(R.id.editPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val backButton = findViewById<Button>(R.id.buttonSetting1)

        // ▼ ログインボタン押下時
        loginButton.setOnClickListener {
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "メールアドレスとパスワードを入力してください", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "ログインしました！", Toast.LENGTH_SHORT).show()
            }
        }

        // ▼ 戻るボタン
        backButton.setOnClickListener {
            val intent = Intent(this, setting::class.java)
            startActivity(intent)
            finish()
        }
    }
}
