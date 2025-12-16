package com.example.meidmappu

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge

class AddEditShopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_edit_shop)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_edit_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // UI要素の取得
        val editShopName = findViewById<EditText>(R.id.edit_shop_name)
        val editAddress = findViewById<EditText>(R.id.edit_address)
        // ... 他の入力フィールドも必要に応じて追加 ...
        val saveButton = findViewById<Button>(R.id.btn_save_shop)
        val backButton = findViewById<Button>(R.id.add_edit_back_button)

        // 戻るボタン
        backButton.setOnClickListener {
            finish()
        }

        // 保存ボタン
        saveButton.setOnClickListener {
            val name = editShopName.text.toString().trim()
            val address = editAddress.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "店名を入力してください。", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 【TODO】DBへの保存（追加/更新）ロジックをここに実装

            Toast.makeText(this, "$name を登録/更新しました！", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}