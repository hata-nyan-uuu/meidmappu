package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ★ すでにログインしていたらMainへ
        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val savedUserId = prefs.getInt("login_user_id", -1)
        if (savedUserId != -1) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val db = UserDatabaseHelper(this)

        val emailEdit = findViewById<EditText>(R.id.editEmail)
        val passwordEdit = findViewById<EditText>(R.id.editPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val backButton = findViewById<Button>(R.id.buttonSetting1)
        val goRegister = findViewById<TextView>(R.id.textGoRegister)

        loginButton.setOnClickListener {
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()

            val userId = db.getUserIdByEmailAndPassword(email, password)

            if (userId != -1) {
                // ★ ログイン成功 → 保存
                prefs.edit()
                    .putInt("login_user_id", userId)
                    .apply()

                Toast.makeText(this, "ログイン成功！", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "メールまたはパスワードが違います", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            finish()
        }

        goRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
