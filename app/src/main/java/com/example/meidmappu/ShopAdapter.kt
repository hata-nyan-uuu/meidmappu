package com.example.meidmappu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShopAdapter(
    private val shopList: List<Shop>,
    private val onItemClick: (Shop) -> Unit   // ← クリック時に呼ばれる関数
) : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.shopImage)
        val shopName: TextView = itemView.findViewById(R.id.shopName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = shopList[position]

        // 店名表示
        holder.shopName.text = shop.name

        // 店舗画像
        val imageResourceId = shop.image ?: R.drawable.noimage // Nullを代替画像IDに変換

        if (imageResourceId != R.drawable.noimage) {
            // 安全なInt型になった imageResourceId を渡す
            holder.image.setImageResource(imageResourceId)
        } else {
            // 画像がない店舗の場合、noimageを設定
            holder.image.setImageResource(R.drawable.noimage)
        }
        // クリック処理
        holder.itemView.setOnClickListener {
            onItemClick(shop)
        }
    }

    override fun getItemCount(): Int = shopList.size
}
