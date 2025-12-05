package com.example.meidmappu.models

data class Review(
    val id: Int,
    val userId: Int,
    val storeId: Int,
    val rating: Int,
    val comment: String
)