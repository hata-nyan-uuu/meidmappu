package com.example.meidmappu

object ShopRepository {

    private val shops = mutableListOf<ShopFirestore>()

    // Firestoreから取得した一覧を保存
    fun setShops(list: List<ShopFirestore>) {
        shops.clear()
        shops.addAll(list)
    }

    // 全件取得（オフライン用）
    fun getAll(): List<ShopFirestore> {
        return shops
    }

    // IDで1件取得
    fun getById(id: String): ShopFirestore? {
        return shops.find { it.id == id }
    }
}
