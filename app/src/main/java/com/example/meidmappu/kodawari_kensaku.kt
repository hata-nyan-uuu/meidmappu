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
            val menu = getSpinnerValue(findViewById(R.id.menu))

            val maxPrice = priceStr?.toIntOrNull()

            Log.d("DEBUG", "検索条件: type=$type maxPrice=$maxPrice concept=$concept menu=$menu")

            val intent = Intent(this, KodawariSearchResult::class.java).apply {
                putExtra("type", type)
                putExtra("maxPrice", maxPrice)
                putExtra("concept", concept)
                putExtra("menu", menu)
            }
            startActivity(intent)
        }
    }

    // Spinnerの選択値を取得（「選択してください」などをnull扱いにする処理も今後追加可能）
    private fun getSpinnerValue(spinner: Spinner): String? {
        val value = spinner.selectedItem.toString()
        return if (value.isBlank() || value == "選択してください") null else value
    }
}
