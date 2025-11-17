package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val db = UserDatabaseHelper(this)

        val emailEdit = findViewById<EditText>(R.id.registerEmail)
        val passEdit = findViewById<EditText>(R.id.registerPassword)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val backButton = findViewById<Button>(R.id.registerBack)

        registerButton.setOnClickListener {

            val email = emailEdit.text.toString()
            val pass = passEdit.text.toString()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "全て入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (db.registerUser(email, pass)) {
                Toast.makeText(this, "登録成功！", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "メールが既に使われています", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}