package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class tyutoriaru03 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tyutoriaru03)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //メイドカフェ
        val nextmeido =findViewById<Button>(R.id.nextmeido)
        nextmeido.setOnClickListener {
            val intent = Intent(this,meido_tyutoriaru::class.java)
            startActivity(intent)
        }
        //コンカフェ
        val nextconcafe =findViewById<Button>(R.id.nextconcafe)
        nextconcafe.setOnClickListener {
            val intent = Intent(this,concafe_tyutoriaru::class.java)
            startActivity(intent)
        }
        //ホーム画面に戻る
        val homeback2=findViewById<ImageButton>(R.id.homeback2)
        homeback2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val backTouchArea=findViewById<ImageButton>(R.id.backTouchArea)
        backTouchArea.setOnClickListener {
            finish()
        }

    }
}