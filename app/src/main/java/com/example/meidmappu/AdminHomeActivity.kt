package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        val btnAddShop = findViewById<Button>(R.id.btnAddShop)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnEditShop= findViewById<Button>(R.id.btnEditShop)

        // お店追加画面へ
        btnAddShop.setOnClickListener {
            startActivity(Intent(this, AdminAddShopActivity::class.java))
        }

        //お店の編集へ
        btnEditShop.setOnClickListener {
            startActivity(Intent(this, AdminShopSearch::class.java))
        }

        // ログアウト
        btnLogout.setOnClickListener {
            val pref = getSharedPreferences("admin", MODE_PRIVATE)
            pref.edit().clear().apply()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
