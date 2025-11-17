package com.example.meidmappu

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "UserDB.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        // ▼ users テーブル作成
        val createTable = """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE,
                password TEXT
            )
        """.trimIndent()

        db.execSQL(createTable)

        // ▼ ★ テストユーザー自動追加 ★
        val insertTestUser = """
            INSERT INTO users (email, password)
            VALUES ('test@example.com', '1234')
        """.trimIndent()

        db.execSQL(insertTestUser)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    // ▼ ユーザー登録（新規登録画面で使用）
    fun insertUser(email: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("email", email)
        values.put("password", password)

        val result = db.insert("users", null, values)
        db.close()
        return result != -1L
    }

    // ▼ ★ registerUser 関数（insertUser の別名として用意）★
    fun registerUser(email: String, password: String): Boolean {
        return insertUser(email, password)
    }

    // ▼ ログインチェック
    fun login(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE email = ? AND password = ?",
            arrayOf(email, password)
        )

        val isLoggedIn = cursor.count > 0
        cursor.close()
        db.close()
        return isLoggedIn
    }
}