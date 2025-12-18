package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class AdminLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idEdit = findViewById<EditText>(R.id.editAdminId)
        val passEdit = findViewById<EditText>(R.id.editAdminPass)
        val loginBtn = findViewById<Button>(R.id.btnAdminLogin)

        val backBtn7 = findViewById<ImageButton>(R.id.backbtn7)
        backBtn7.setOnClickListener { finish() }

        loginBtn.setOnClickListener {
            val id = idEdit.text.toString()
            val pass = passEdit.text.toString()

            if (id == "abc" && pass == "1234") {
                // ログイン成功
                val pref = getSharedPreferences("admin", MODE_PRIVATE)
                pref.edit().putBoolean("login", true).apply()

                startActivity(Intent(this, AdminHomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "IDかパスワードが違います", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
