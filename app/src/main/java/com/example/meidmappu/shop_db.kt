package com.example.meidmappu

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class shop_dbActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_db)

        val container = findViewById<LinearLayout>(R.id.shopContainer)
        val url = "http://10.0.2.2/api/get_data.php" // ローカルサーバー

        val queue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(url,
            { response ->
                for (i in 0 until response.length()) {
                    val shop = response.getJSONObject(i)
                    val view = layoutInflater.inflate(R.layout.item_shop, container, false)

                    val nameView = view.findViewById<TextView>(R.id.shopName)
                    val addressView = view.findViewById<TextView>(R.id.shopAddress)
                    val conceptView = view.findViewById<TextView>(R.id.shopConcept)
                    val menuView = view.findViewById<TextView>(R.id.shopMenu)
                    val feelingView = view.findViewById<TextView>(R.id.shopFeeling)
                    val typeView = view.findViewById<TextView>(R.id.shopType)
                    val priceView = view.findViewById<TextView>(R.id.shopPrice)
                    val timesView = view.findViewById<TextView>(R.id.shopTimes)

                    // JSON データをセット
                    nameView.text = "店名: ${shop.getString("name")}"
                    addressView.text = "住所: ${shop.getString("address")}"
                    conceptView.text = "コンセプト: ${shop.getString("concept")}"
                    menuView.text = "メニュー: ${shop.getString("menu")}"
                    feelingView.text = "雰囲気: ${shop.getString("feeling")}"
                    typeView.text = "種類: ${shop.getString("type")}"
                    priceView.text = "価格帯: ${shop.getString("price_range")}"
                    timesView.text = "営業時間: ${shop.getString("times")}"

                    // LinearLayout に追加
                    container.addView(view)
                }
            },
            { error ->
                error.printStackTrace()
            })

        queue.add(request)
    }
}
