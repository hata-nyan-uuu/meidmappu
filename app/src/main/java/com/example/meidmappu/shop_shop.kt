package com.example.meidmappu

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView


class shop_shop : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_shop)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val name = intent.getStringExtra("shop_name")
        val address = intent.getStringExtra("shop_address")
        val feeling = intent.getStringExtra("shop_feeling")

        findViewById<TextView>(R.id.shop_name).text = name
        findViewById<TextView>(R.id.shop_address).text = address
        findViewById<TextView>(R.id.shop_description).text = feeling

    }
}