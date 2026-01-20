package com.example.meidmappu

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class AdminEditShopActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var shopId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ===== Edge to Edge =====
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_add_shop)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val paddingInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.ime()
            )
            v.setPadding(
                paddingInsets.left,
                paddingInsets.top,
                paddingInsets.right,
                paddingInsets.bottom
            )
            insets
        }

        // ===== shopId 取得 =====
        shopId = intent.getStringExtra("shopId") ?: run {
            Toast.makeText(this, "店舗IDが取得できません", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // ===== View =====
        val editName = findViewById<EditText>(R.id.editName)
        val editAddress = findViewById<EditText>(R.id.editAddress)
        val editTime = findViewById<EditText>(R.id.editTime)
        val editImage = findViewById<EditText>(R.id.editImage)
        val editMenu = findViewById<EditText>(R.id.editMenu)
        val editWebsite = findViewById<EditText>(R.id.edit_website)
        val editInstagram = findViewById<EditText>(R.id.edit_instagram)
        val editX = findViewById<EditText>(R.id.edit_x)
        val editTikTok = findViewById<EditText>(R.id.edit_tiktok)

        val spinnerType = findViewById<Spinner>(R.id.spinnerType)
        val spinnerConcept = findViewById<Spinner>(R.id.spinnerConcept)
        val spinnerFeeling = findViewById<Spinner>(R.id.spinnerFeeling)
        val spinnerPrice = findViewById<Spinner>(R.id.spinnerPrice)

        val btnSave = findViewById<Button>(R.id.btnSaveShop)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        // ===== Firestore から既存データ読み込み =====
        db.collection("shop")
            .document(shopId)
            .get()
            .addOnSuccessListener { doc ->
                editName.setText(doc.getString("name"))
                editAddress.setText(doc.getString("address"))
                editTime.setText(doc.getString("time"))
                editImage.setText(doc.getString("image"))
                editMenu.setText(doc.getString("menu"))
                editWebsite.setText(doc.getString("website"))
                editInstagram.setText(doc.getString("instagram"))
                editX.setText(doc.getString("x"))
                editTikTok.setText(doc.getString("tiktok"))

                setSpinner(spinnerType, doc.getString("type"))
                setSpinner(spinnerConcept, doc.getString("concept"))
                setSpinner(spinnerFeeling, doc.getString("feeling"))
                setSpinnerPrice(spinnerPrice, doc.getLong("priceRange"))
            }

        // ===== 保存（更新） =====
        btnSave.setOnClickListener {

            if (
                spinnerType.selectedItemPosition == 0 ||
                spinnerConcept.selectedItemPosition == 0 ||
                spinnerFeeling.selectedItemPosition == 0 ||
                spinnerPrice.selectedItemPosition == 0
            ) {
                Toast.makeText(this, "すべて選択してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = when (spinnerPrice.selectedItemPosition) {
                1 -> 3000
                2 -> 5000
                3 -> 10000
                else -> 0
            }

            val shopData: MutableMap<String, Any> = mutableMapOf(
                "name" to editName.text.toString(),
                "address" to editAddress.text.toString(),
                "time" to editTime.text.toString(),
                "image" to editImage.text.toString(),
                "menu" to editMenu.text.toString(),
                "website" to editWebsite.text.toString(),
                "instagram" to editInstagram.text.toString(),
                "x" to editX.text.toString(),
                "tiktok" to editTikTok.text.toString(),
                "priceRange" to price,
                "type" to spinnerType.selectedItem.toString(),
                "concept" to spinnerConcept.selectedItem.toString(),
                "feeling" to spinnerFeeling.selectedItem.toString()
            )


            db.collection("shop")
                .document(shopId)
                .update(shopData)
                .addOnSuccessListener {
                    Toast.makeText(this, "お店情報を更新しました", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "更新に失敗しました", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // ===== Spinner 補助 =====
    private fun setSpinner(spinner: Spinner, value: String?) {
        if (value == null) return
        for (i in 0 until spinner.adapter.count) {
            if (spinner.adapter.getItem(i).toString() == value) {
                spinner.setSelection(i)
                break
            }
        }
    }

    private fun setSpinnerPrice(spinner: Spinner, price: Long?) {
        when (price) {
            3000L -> spinner.setSelection(1)
            5000L -> spinner.setSelection(2)
            10000L -> spinner.setSelection(3)
        }
    }
}
