package com.example.meidmappu

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ローカルデータベース（Shopデータを保存）
@Database(entities = [Shop::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shopDao(): ShopDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // アプリ全体で1つのDBインスタンスを使う
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shop_database"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }

        // データベースが初めて作られたときに呼ばれる処理
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.let { database ->
                        prepopulateDatabase(database.shopDao())
                    }
                }
            }
        }

        //  初期データを挿入するメソッド
        private suspend fun prepopulateDatabase(dao: ShopDao) {
            val shops = listOf(
                Shop(
                    name = "こもれび亭",
                    address = "東京都千代田区外神田",
                    feeling = "ゆっくりしたい",
                    concept = "メイド系",
                    type = "メイドカフェ",
                    menu = "コーヒー、抹茶ラテ、和菓子セット",
                    price_range = 3000,
                    times = "10:00~22:00",
                    image =R.drawable.image_fx__1_,
                    image2 = R.drawable.komorebi
                ),
                Shop(
                    name = "メルシーメイド",
                    address = "東京都千代田区外神田",
                    feeling = "ごはんをたべたい",
                    concept = "メイド系",
                    type = "メイドカフェ",
                    menu = "マカロン、パン、タルト",
                    price_range = 5000,
                    times = "10:00~22:00",
                    image = R.drawable.image_fx,
                    image2 = R.drawable.merusiMeido
                ),
                Shop(
                    name = "ハートフル・エンジェル",
                    address = "東京都千代田区外神田",
                    feeling = "だれかとはなしたい",
                    concept = "天使系",
                    type = "メイドカフェ",
                    menu = "オムライス、ナポリタン、パフェ",
                    price_range = 5000,
                    times = "10:00~22:00",
                    image = R.drawable.image_fx__2_,
                    image2 = R.drawable.haatofuru
                ),
                Shop(
                    name = "ノワールアンジュ",
                    address = "東京都千代田区外神田",
                    feeling = "だれかとはなしたい",
                    concept = "天使系",
                    type = "コンカフェ",
                    menu = "オリジナルシャンパン、おつまみ",
                    price_range = 5000,
                    times = "17:00~24:00",
                    image = R.drawable.image_fx__2_,
                    image2 = R.drawable.nowaaruAnjuu
                ),
                Shop(
                    name = "デビルリリィ",
                    address = "東京都千代田区外神田",
                    feeling = "だれかとはなしたい",
                    concept = "悪魔系",
                    type = "コンカフェ",
                    menu = "オリジナルシャンパン、おつまみ",
                    price_range = 5000,
                    times = "17:00~24:00",
                    image = R.drawable.image_fx__2_,
                    image2 = R.drawable.nowaaruAnjuu
                ),
                Shop(
                    name = "リトルメイドハウス",
                    address = "東京都千代田区外神田",
                    feeling = "ゆっくりしたい",
                    concept = "メイド系",
                    type = "コンカフェ",
                    menu = "オリジナルシャンパン、おつまみ",
                    price_range = 5000,
                    times = "17:00~24:00",
                    image = R.drawable.image_fx__2_,
                    image2 = R.drawable.nowaaruAnjuu
                )
            )
            shops.forEach { dao.insert(it) }
        }
    }
}
