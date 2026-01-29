package com.example.meidmappu.models

data class Review(
    val id: String = "",        // Firestore のドキュメントID
    val userId: String = "",
    val name: String="",
    val rating: Int = 0,
    val comment: String = "",
    val timestamp: Long = 0     // 作成時刻
)