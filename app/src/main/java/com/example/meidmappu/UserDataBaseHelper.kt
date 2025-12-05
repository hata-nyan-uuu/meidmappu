package com.example.meidmappu

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class UserDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "user_local.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        // 店テーブル
        db.execSQL("""
            CREATE TABLE stores (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                address TEXT,
                description TEXT
            )
        """)

        // ユーザーテーブル
        db.execSQL("""
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT,
                password TEXT
            )
        """)

        // レビューテーブル
        db.execSQL("""
            CREATE TABLE reviews (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                store_id INTEGER,
                rating INTEGER,
                comment TEXT
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    // --- 店取得 ---
    fun getStoreById(id: Int): Store? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM stores WHERE id = ?", arrayOf(id.toString()))
        return if (cursor.moveToFirst()) {
            val store = Store(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("address")),
                cursor.getString(cursor.getColumnIndexOrThrow("description"))
            )
            cursor.close()
            store
        } else {
            cursor.close()
            null
        }
    }

    // --- レビュー登録 ---
    fun insertReview(userId: Int, storeId: Int, rating: Int, comment: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("user_id", userId)
            put("store_id", storeId)
            put("rating", rating)
            put("comment", comment)
        }
        val result = db.insert("reviews", null, values)
        return result != -1L
    }

    // --- レビュー一覧取得 ---
    fun getReviewsByStoreId(storeId: Int): ArrayList<Review> {
        val list = ArrayList<Review>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM reviews WHERE store_id = ? ORDER BY id DESC",
            arrayOf(storeId.toString())
        )

        while (cursor.moveToNext()) {
            list.add(
                Review(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("store_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("rating")),
                    cursor.getString(cursor.getColumnIndexOrThrow("comment"))
                )
            )
        }

        cursor.close()
        return list
    }

    // --- ログイン（成功なら true、SharedPreferencesに保存するのはActivity側で） ---
    fun login(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id FROM users WHERE email = ? AND password = ?",
            arrayOf(email, password)
        )

        val success = cursor.moveToFirst()
        cursor.close()
        return success
    }

    // --- ログイン時にユーザーID取得 ---
    fun getUserIdByEmailAndPassword(email: String, password: String): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id FROM users WHERE email = ? AND password = ?",
            arrayOf(email, password)
        )
        val userId = if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        } else {
            -1
        }
        cursor.close()
        return userId
    }

    // --- ユーザー登録（成功なら true） ---
    fun registerUser(email: String, password: String): Boolean {
        val db = writableDatabase

        // 重複チェック
        val cursor = db.rawQuery(
            "SELECT id FROM users WHERE email = ?",
            arrayOf(email)
        )

        if (cursor.moveToFirst()) {
            cursor.close()
            return false
        }
        cursor.close()

        // 新規追加
        val values = ContentValues().apply {
            put("email", email)
            put("password", password)
        }

        val result = db.insert("users", null, values)
        return result != -1L
    }
}

// =========================
// データクラス
// =========================
data class Store(
    val id: Int,
    val name: String,
    val address: String,
    val description: String
)

data class Review(
    val id: Int,
    val userId: Int,
    val storeId: Int,
    val rating: Int,
    val comment: String
)
