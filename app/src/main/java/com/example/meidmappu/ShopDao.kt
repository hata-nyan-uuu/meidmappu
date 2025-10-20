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
}
