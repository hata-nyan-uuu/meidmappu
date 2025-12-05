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

            // --- ユーザーIDを取得 ---
            val userId = db.getUserIdByEmailAndPassword(email, password)

            if (userId != -1) {
                // ログイン成功
                Toast.makeText(this, "ログイン成功！", Toast.LENGTH_SHORT).show()

                // SharedPreferences に保存
                val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
                prefs.edit().putInt("login_user_id", userId).apply()

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
