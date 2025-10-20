package com.example.meidmappu

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// ✅ データベースに含めるエンティティ（テーブル）とバージョンを指定
@Database(entities = [Shop::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // DAOを呼び出す関数（必須）
    abstract fun shopDao(): ShopDao

    companion object {
        // シングルトン（アプリ内で1つのDBインスタンスだけを共有）
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shop_database" // ← アプリ内で作られるSQLiteファイル名
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
