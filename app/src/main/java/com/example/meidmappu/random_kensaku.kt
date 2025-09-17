package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class random_kensaku : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_random_kensaku)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //前の画面にもどる
        val back01 =findViewById<Button>(R.id.back01)
        back01.setOnClickListener { finish() }
        //気持ちがめんへ
        val kibun = findViewById<Button>(R.id.kibun)
        kibun.setOnClickListener {
            val intent = Intent(this,kibun_sentaku::class.java)
            startActivity(intent)
        }
        //コンセプト選択へ
        val conseputo = findViewById<Button>(R.id.conseputo)
        conseputo.setOnClickListener {
            val intent = Intent(this,conseputo_kensaku::class.java)
            startActivity(intent)
        }
    }
}