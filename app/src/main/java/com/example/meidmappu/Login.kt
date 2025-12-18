package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        // ログインしていたらMainへ
        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val savedUserId = prefs.getInt("login_user_id", -1)
        if (savedUserId != -1) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val emailEdit = findViewById<EditText>(R.id.editEmail)
        val passwordEdit = findViewById<EditText>(R.id.editPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val backbtn8 = findViewById<ImageButton>(R.id.backbtn8)
        val goRegister = findViewById<TextView>(R.id.textGoRegister)

        loginButton.setOnClickListener {
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()

            // ↓ あとでDB接続したらここ復活でOK
            // val userId = db.getUserIdByEmailAndPassword(email, password)

            /*
            if (userId != -1) {
                prefs.edit()
                    .putInt("login_user_id", userId)
                    .apply()

                Toast.makeText(this, "ログイン成功！", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "メールまたはパスワードが違います", Toast.LENGTH_SHORT).show()
            }
            */
        }

        backbtn8.setOnClickListener {
            finish()
        }

        goRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
