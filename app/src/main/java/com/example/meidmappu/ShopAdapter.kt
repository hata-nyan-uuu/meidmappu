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
        val shopImage: ImageView = itemView.findViewById(R.id.shopImage)
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

        // 画像設定（仮）後で差し替え → 今は仮画像を利用
       holder.shopImage.setImageResource(R.drawable.image_fx)

        // クリック処理
        holder.itemView.setOnClickListener {
            onItemClick(shop)
        }
    }

    override fun getItemCount(): Int = shopList.size
}
