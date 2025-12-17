package com.example.meidmappu


data class ShopFirestore(
    var id: String = "",
    var name: String = "",
    var address: String = "",
    var feeling: String = "",
    var concept: String = "",
    var menu: String? = null,
    var type: String = "",
    var priceRange: Long = 0L,
    var time: String? = null,
    var image: String? = null
)
