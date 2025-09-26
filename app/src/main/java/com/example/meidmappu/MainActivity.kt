package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //らんだむけんさく画面移動
        val random1 = findViewById<Button>(R.id.random1)
        random1.setOnClickListener {
            val intent = Intent(this, random_kensaku::class.java)
            startActivity(intent)
        }
        //こだわりけんさく画面に移動
        val kodawari =findViewById<Button>(R.id.kodawari)
        kodawari.setOnClickListener {
            val intent = Intent(this,kodawari_kensaku::class.java)
            startActivity(intent)
        }

        //チュートリアル画面に移動
        val hazimete = findViewById<Button>(R.id.hazimete)
            hazimete.setOnClickListener {
                val intent = Intent(this,tyutoriaru01::class.java)
                startActivity(intent)
            }
    }
}