package com.example.meidmappu
import android.graphics.drawable.GradientDrawable
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    // 画像と店舗名の対応リスト（1対1）
    private val shopMap = mapOf(
        R.id.omise01 to "こもれび亭",
        R.id.omise02 to "メルシーメイド",
        R.id.omise03 to "ハートフル・エンジェル"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // ステータスバー・ナビゲーションバー分の余白を調整
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // データベース初期化
        db = AppDatabase.getDatabase(this)

        // 各画像にクリックイベントを設定
        shopMap.forEach { (imageId, shopName) ->
            findViewById<ImageView>(imageId).setOnClickListener {
                openShopDetail(shopName)
            }
        }

        // ボタン処理（ナビゲーション）
        setButtonListeners()
    }

    /** 詳細画面を開く処理 */
    private fun openShopDetail(shopName: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val shop = db.shopDao().getShopByName(shopName)
            withContext(Dispatchers.Main) {
                if (shop == null) {
                    showToast("データが見つかりませんでした。")
                    return@withContext
                }

                val intent = Intent(this@MainActivity, shop_shop::class.java).apply {
                    putExtra("shop_name", shop.name)
                    putExtra("shop_address", shop.address)
                    putExtra("shop_feeling", shop.feeling)
                    putExtra("shop_concept", shop.concept)
                   /* putExtra("shop_menu", shop.menu)
                    putExtra("shop_type", shop.type)
                    putExtra("shop_price_range", shop.price_range)
                    putExtra("shop_times", shop.times)*/
                }
                startActivity(intent)
            }
        }
    }

    /** トースト表示を共通化 */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 各ボタンの処理をまとめて設定
    private fun setButtonListeners() {
        val random1=findViewById<ImageButton>(R.id.random1)
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 24f.dp(this@MainActivity) // ← 角の丸み
            setColor(Color.WHITE) // 背景色（必要に応じて変更）
        }
            random1.setOnClickListener {
            startActivity(Intent(this, RandomKensaku::class.java))
        }
        findViewById<Button>(R.id.kodawari).setOnClickListener {
            startActivity(Intent(this, kodawari_kensaku::class.java))
        }
        findViewById<Button>(R.id.hazimete).setOnClickListener {
            startActivity(Intent(this, tyutoriaru01::class.java))
        }
        findViewById<Button>(R.id.settingbtn).setOnClickListener {
            startActivity(Intent(this,setting::class.java))
        }
    }
}
