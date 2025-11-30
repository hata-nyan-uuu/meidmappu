package com.example.meidmappu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShopAdapter2(
    private val shopList: List<Shop>,
    private val onClick: (Shop) -> Unit
) : RecyclerView.Adapter<ShopAdapter2.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.shopImage)
        val name: TextView = view.findViewById(R.id.shopName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shop = shopList[position]

        holder.name.text = shop.name

        // 店舗画像
        val imageResourceId = shop.image ?: R.drawable.noimage // Nullを代替画像IDに変換

        if (imageResourceId != R.drawable.noimage) {
            // 安全なInt型になった imageResourceId を渡す
            holder.image.setImageResource(imageResourceId)
        } else {
            // 画像がない店舗の場合、noimageを設定
            holder.image.setImageResource(R.drawable.noimage)
        }

        holder.itemView.setOnClickListener { onClick(shop) }
    }

    override fun getItemCount(): Int = shopList.size
}
