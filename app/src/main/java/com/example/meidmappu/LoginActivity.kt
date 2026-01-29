package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    //shop_shopからのデータを受け取る
    private fun onLoginSuccess() {
        val returnTo = intent.getStringExtra("RETURN_TO")
        val returnShopId = intent.getStringExtra("shopId")

        if (returnTo == "SHOP" && returnShopId != null) {
            startActivity(
                Intent(this, shop_shop::class.java).apply {
                    putExtra("shopId", returnShopId)
                }
            )
        } else {
            // 通常ログイン（ホームへ）
            startActivity(Intent(this, MainActivity::class.java))
        }

        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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

        // FirebaseAuth 初期化
        auth = FirebaseAuth.getInstance()

        // すでにログインしていたら MainActivity へ
        if (auth.currentUser != null) {
            onLoginSuccess()
            return
        }



        // View取得
        val emailEdit = findViewById<EditText>(R.id.editEmail)
        val passwordEdit = findViewById<EditText>(R.id.editPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val backbtn8 = findViewById<ImageButton>(R.id.backbtn8)
        val goRegister = findViewById<TextView>(R.id.textGoRegister)

        // ログイン処理
        loginButton.setOnClickListener {

            val email = emailEdit.text.toString().trim()
            val password = passwordEdit.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "メールアドレスとパスワードを入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "正しいメールアドレスを入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "ログイン成功！", Toast.LENGTH_SHORT).show()
                        onLoginSuccess()
                    } else {
                        Toast.makeText(
                            this,
                            "ログイン失敗: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // 戻る
        backbtn8.setOnClickListener {
            finish()
        }

        // 新規登録へ
        goRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
