package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class setting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.buttonSetting4).setOnClickListener {
            startActivity(Intent(this,ContactActivity::class.java))
        }
        // ▼ 「ログイン」ボタン（buttonSetting2）を取得
        val loginButton = findViewById<Button>(R.id.buttonSetting2)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        // ★ メイン画面へ戻るボタン（例: buttonSetting1 として追加）
        findViewById<Button>(R.id.buttonSetting1).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // ← 戻った後に設定画面を消す
            startActivity(intent)
        }
        //管理者ログイン
        findViewById<Button>(R.id.buttonSetting5).setOnClickListener {
            val intent= Intent(this, AdminLoginActivity::class.java)
            startActivity(intent)
        }
    }
}