package com.example.meidmappu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

// このファイルにこれだけが書かれている状態にする
class HeaderAdapter(private val onHeaderClick: (Int) -> Unit) :
    RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btnSetting: ImageButton = view.findViewById(R.id.settingbtn)
        val cardHazimete: MaterialCardView = view.findViewById(R.id.hazimete)
        val cardKodawari: MaterialCardView = view.findViewById(R.id.kodawari)
        val cardRandom: MaterialCardView = view.findViewById(R.id.random1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.header_home, parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.btnSetting.setOnClickListener { onHeaderClick(R.id.settingbtn) }
        holder.cardHazimete.setOnClickListener { onHeaderClick(R.id.hazimete) }
        holder.cardKodawari.setOnClickListener { onHeaderClick(R.id.kodawari) }
        holder.cardRandom.setOnClickListener { onHeaderClick(R.id.random1) }
    }

    override fun getItemCount() = 1
}