package com.example.meidmappu

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Shop::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shopDao(): ShopDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shop_database"
                )
                    // 初回作成時にデータを入れるCallbackを追加
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getDatabase(context).shopDao()
                                dao.insert(Shop(name = "こもれび亭", address = "東京都千代田区外神田", feeling = "ゆっくりしたい", concept = "", type = "", menu = "", price_range = 3000, times = ""))
                                dao.insert(Shop(name = "メルシーメイド", address = "東京都千代田区外神田", feeling = "ごはんをたべたい", concept = "", type = "", menu = "", price_range = 3000, times = ""))
                                dao.insert(Shop(name = "ハートフル・エンジェル", address = "東京都千代田区外神田", feeling = "だれかとはなしたい" , concept = "", type = "", menu = "", price_range = 3000, times = ""))
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
