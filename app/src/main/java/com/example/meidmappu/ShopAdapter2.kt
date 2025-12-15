package com.example.meidmappu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ShopFirestoreAdapter(
    private val shopList: List<ShopFirestore>,
    private val onClick: (ShopFirestore) -> Unit
) : RecyclerView.Adapter<ShopFirestoreAdapter.ViewHolder>() {

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

        // Firestore の image フィールドを使用
        val imageUrl = shop.image
            ?: "https://imgur.com/oYta9hf"

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.noimage)
            .error(R.drawable.noimage)
            .into(holder.image)

        holder.itemView.setOnClickListener { onClick(shop) }
    }

    override fun getItemCount(): Int = shopList.size
}
