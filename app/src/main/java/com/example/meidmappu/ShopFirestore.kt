package com.example.meidmappu


data class ShopFirestore(
    var name: String = "",
    var address: String = "",
    var feeling: String = "",
    var concept: String = "",
    var menu: String? = null,
    var type: String = "",
    var priceRange: Int = 0,
    var time: String? = null,
    var image: String? = null
)
