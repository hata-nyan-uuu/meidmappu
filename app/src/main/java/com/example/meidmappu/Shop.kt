package com.example.meidmappu
//お店ローカルDBの変数決め設計図みたいなもん

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shops")
data class Shop(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String,
    val feeling: String,
    val concept: String,
    val menu: String,
    val type: String,
    val price_range: Int,
    val times: String?
)
