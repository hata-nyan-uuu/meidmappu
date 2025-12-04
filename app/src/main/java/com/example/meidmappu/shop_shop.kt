package com.example.meidmappu
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.content.Intent


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
        //うけとる
        val name = intent.getStringExtra("shopName")
        val address = intent.getStringExtra("shopAddress")
        val image1Id = intent.getIntExtra("image1", 0)
        val image2Id = intent.getIntExtra("image2", 0)


        findViewById<TextView>(R.id.shop_name).text = name
        val mainImage: ImageView = findViewById(R.id.imageView4)
        val menuImage: ImageView = findViewById(R.id.imageView5)

        // 3. 画像を設定 (IDが0でない場合のみsetImageResourceを実行)
        if (image1Id != 0) {
            mainImage.setImageResource(image1Id)
        }
        if (image2Id != 0) {
            menuImage.setImageResource(image2Id)
        }
        //住所
        val addressTextView: TextView = findViewById(R.id.shop_address)
        // 住所を設定（Nullチェック）
        if (!address.isNullOrEmpty()) {
            addressTextView.text = "住所: $address (タップでマップを開く)"

            // ★★★ 3. クリックリスナーを設定し、マップを起動する ★★★
            addressTextView.setOnClickListener {
                // ジオURIスキームを使用してGoogleマップを開く
                // q=address で住所を検索
                val mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address))
                val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)

                // Googleマップアプリが存在するか確認
                mapIntent.setPackage("com.google.android.apps.maps")

                // マップアプリがない場合でも、ブラウザで開けるようにチェック
                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent)
                } else {
                    // Googleマップがない場合は、一般的なACTION_VIEWでブラウザで開く
                    val generalMapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                    startActivity(generalMapIntent)
                }
            }
        } else {
            // 住所データがない場合はTextViewを非表示に
            addressTextView.text = "住所情報がありません"
        }

    }
}