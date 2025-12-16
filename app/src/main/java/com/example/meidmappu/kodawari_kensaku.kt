package com.example.meidmappu

import android.util.Log
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class kodawari_kensaku : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kodawari_kensaku)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //前の画面にもどる
        val back01 = findViewById<ImageButton>(R.id.backbtn2)
        back01.setOnClickListener { finish() }

        // 検索ボタン
        findViewById<Button>(R.id.kensaku).setOnClickListener {
            val type = getSpinnerValue(findViewById(R.id.mise_type))
            val priceStr = getSpinnerValue(findViewById(R.id.kakaku))
            val concept = getSpinnerValue(findViewById(R.id.conseputo2))
            val feeling = getSpinnerValue(findViewById(R.id.feeling))

            val priceRange = priceStr
                ?.replace(",", "")
                ?.replace("円", "")
                ?.toLongOrNull()

            Log.d("DEBUG", "検索条件: type=$type priceRange=$priceRange concept=$concept feeling=$feeling")

            val intent = Intent(this, KodawariSearchResult::class.java).apply {
                putExtra("type", type)
                putExtra("priceRange", priceRange)
                putExtra("concept", concept)
                putExtra("feeling", feeling)
            }
            startActivity(intent)
        }
    }

    // Spinner の先頭項目は null（未選択）として扱う安全版
    private fun getSpinnerValue(spinner: Spinner): String? {
        return if (spinner.selectedItemPosition == 0) {
            null
        } else {
            spinner.selectedItem.toString()
        }
    }
}
