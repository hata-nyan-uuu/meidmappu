package com.example.meidmappu

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        val editName = findViewById<EditText>(R.id.editName)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editMessage = findViewById<EditText>(R.id.editMessage)
        val buttonSend = findViewById<Button>(R.id.buttonSend)
        val buttonBack = findViewById<Button>(R.id.buttonBack)

        buttonSend.setOnClickListener {
            val name = editName.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val message = editMessage.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "すべての項目を入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ここに送信処理（例：サーバーへPOST、メールアプリ起動 等）を追加できます。
            // とりあえず確認メッセージを出して戻る動作にします：
            Toast.makeText(this, "お問い合わせを送信しました（ダミー）", Toast.LENGTH_SHORT).show()
            finish()
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }
}
