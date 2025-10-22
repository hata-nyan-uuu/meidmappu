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
                                dao.insert(Shop(name = "カフェ東京", address = "東京都渋谷区", description = "おしゃれなカフェ"))
                                dao.insert(Shop(name = "寿司太郎", address = "東京都新宿区", description = "新鮮なネタが自慢"))
                                dao.insert(Shop(name = "パン工房花", address = "東京都世田谷区", description = "焼き立てパンが人気"))
                            }
                        }
                    })

                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
