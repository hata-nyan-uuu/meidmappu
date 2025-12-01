package com.example.meidmappu

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.admin_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 戻るボタンの処理
        findViewById<Button>(R.id.admin_back_button).setOnClickListener {
            finish() // 画面を閉じて前の設定画面に戻る
        }

        // 【TODO】お店の追加/編集画面への遷移ロジックをここに実装
        // findViewById<Button>(R.id.btn_add_edit_shop).setOnClickListener {
        //     // ...
        // }
    }
}