package com.example.meidmappu

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update

@Dao
interface ShopDao {

    // 全データ取得
    @Query("SELECT * FROM shops")
    suspend fun getAll(): List<Shop>

    // データ追加
    @Insert
    suspend fun insert(shop: Shop)

    // データ削除
    @Delete
    suspend fun delete(shop: Shop)

    // 全削除
    @Query("DELETE FROM shops")
    suspend fun deleteAll()

    // データ更新
    @Update
    suspend fun update(shop: Shop)

    // 店名から1件だけ取得
    @Query("SELECT * FROM shops WHERE name = :name LIMIT 1")
    suspend fun getShopByName(name: String): Shop?

    // 複数条件検索（部分一致対応）
    @Query("""
    SELECT * FROM shop
    WHERE (:type IS NULL OR type = :type)
      AND (:maxPrice IS NULL OR price_range <= :maxPrice)
      AND (:concept IS NULL OR concept LIKE '%' || :concept || '%')
      AND (:feeling IS NULL OR feeling LIKE '%' || :feeling || '%')
""")
    suspend fun filterShops(
        type: String?,
        maxPrice: Int?,
        concept: String?,
        feeling: String?   // ← menu ではなく feeling に変更
    ): List<Shop>
}
