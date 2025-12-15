package com.example.meidmappu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Button
import android.content.Intent
import android.widget.Toast



class AdminLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        val idEdit = findViewById<EditText>(R.id.editAdminId)
        val passEdit = findViewById<EditText>(R.id.editAdminPass)
        val loginBtn = findViewById<Button>(R.id.btnAdminLogin)

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
