package com.example.meidmappu

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide

class shop_dbActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_db)

        val container = findViewById<LinearLayout>(R.id.shopContainer)
        val url = "http://192.168.0.10/meid_app/get_shops.php" // ←あなたのPCのIPアドレスに置き換えて！

        val queue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(url,
            { response ->
                for (i in 0 until response.length()) {
                    val shop = response.getJSONObject(i)
                    val view = layoutInflater.inflate(R.layout.item_shop, container, false)

                    val nameView = view.findViewById<TextView>(R.id.shopName)
                    val menuView = view.findViewById<TextView>(R.id.shopMenu)
                    val priceView = view.findViewById<TextView>(R.id.shopPrice)
                    val imageView = view.findViewById<ImageView>(R.id.shopImage)

                    nameView.text = shop.getString("name")
                    menuView.text = shop.getString("menu")
                    priceView.text = "¥${shop.getInt("price")}"

                    Glide.with(this)
                        .load(shop.getString("image_url"))
                        .into(imageView)

                    container.addView(view)
                }
            },
            { error ->
                error.printStackTrace()
            })

        queue.add(request)
    }
}
