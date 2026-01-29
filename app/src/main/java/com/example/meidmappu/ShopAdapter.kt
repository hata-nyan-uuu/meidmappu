package com.example.meidmappu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class ShopAdapter(

    private val shopList: List<ShopFirestore>,
    private val onItemClick: (ShopFirestore) -> Unit
) : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {
    private fun normalizeTilde(text: String?): String {
        return text
            ?.replace("〜", "～") // 波ダッシュ → 全角チルダ
            ?.replace("~","～") //半角から全角チルダ
            ?: ""
    }

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

        holder.shopName.text = normalizeTilde(shop.name)

        // Firestore の image URL
        val imageUrl = shop.image ?: "https://i.imgur.com/oYta9hf.jpeg"

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.noimage)
            .error(R.drawable.noimage)
            .centerCrop()                     // アスペクト比を維持して ImageView に収める
            .diskCacheStrategy(DiskCacheStrategy.ALL) // キャッシュを強化
            .into(holder.image)

        holder.itemView.setOnClickListener { onItemClick(shop) }
    }

    override fun getItemCount(): Int = shopList.size
}

