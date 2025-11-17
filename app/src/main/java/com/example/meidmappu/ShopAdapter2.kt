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

        // 店舗画像（DBに画像IDがあるなら差し替え）
       // holder.image.setImageResource(shop.image)

        holder.itemView.setOnClickListener { onClick(shop) }
    }

    override fun getItemCount(): Int = shopList.size
}
