package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class setting : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var loginButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_setting)
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

        auth = FirebaseAuth.getInstance()
        loginButton = findViewById(R.id.buttonSetting2)

        // お問い合わせ
        findViewById<ImageButton>(R.id.buttonSetting4).setOnClickListener {
            startActivity(Intent(this, ContactActivity::class.java))
        }

        // 戻る
        findViewById<ImageButton>(R.id.buttonSetting1).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
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

        // 管理者ログイン
        findViewById<ImageButton>(R.id.buttonSetting5).setOnClickListener {
            startActivity(Intent(this, AdminLoginActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()


        if (auth.currentUser != null) {
            // ログイン中
            loginButton.setImageResource(R.drawable.mypagebtn)
            loginButton.setOnClickListener {
                startActivity(Intent(this, AccountSetting::class.java))
            }
        } else {
            // 未ログイン
            loginButton.setImageResource(R.drawable.login)
            loginButton.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
}
