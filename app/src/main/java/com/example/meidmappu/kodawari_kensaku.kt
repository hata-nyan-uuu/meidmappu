package com.example.meidmappu
import android.util.Log
import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

        // 前の画面に戻る
        findViewById<Button>(R.id.back04).setOnClickListener {
            finish()
        }

        // 検索ボタン
        findViewById<Button>(R.id.kensaku).setOnClickListener {
            val type = getSpinnerValue(findViewById(R.id.mise_type))
            val priceStr = getSpinnerValue(findViewById(R.id.kakaku))
            val concept = getSpinnerValue(findViewById(R.id.conseputo2))
            val feeling = getSpinnerValue(findViewById(R.id.feeling))

            // 表示用の文字列から「円」と「カンマ」を除去して整数に変換
            val maxPrice = priceStr?.replace(",", "")?.replace("円", "")?.toIntOrNull()

            Log.d("DEBUG", "検索条件: type=$type maxPrice=$maxPrice concept=$concept feeling=$feeling")

            val intent = Intent(this, KodawariSearchResult::class.java).apply {
                putExtra("type", type)
                putExtra("maxPrice", maxPrice)
                putExtra("concept", concept)
                putExtra("feeling", feeling)
            }
            startActivity(intent)
        }
    }

    private fun getSpinnerValue(spinner: Spinner): String? {
        val value = spinner.selectedItem.toString()
        // Spinner の先頭行は未選択扱いにする
        return when (value) {
            "お店のタイプは？", "予算は？", "コンセプトは？", "いまの気分は？" -> null
            else -> value
        }
    }

}
