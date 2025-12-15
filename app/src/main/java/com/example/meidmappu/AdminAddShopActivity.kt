package com.example.meidmappu

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AdminAddShopActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_shop)

        // EditText
        val editName = findViewById<EditText>(R.id.editName)
        val editAddress = findViewById<EditText>(R.id.editAddress)
        val editTime = findViewById<EditText>(R.id.editTime)
        val editImage = findViewById<EditText>(R.id.editImage)
        val editMenu = findViewById<EditText>(R.id.editMenu)

        // Spinner
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)
        val spinnerConcept = findViewById<Spinner>(R.id.spinnerConcept)
        val spinnerFeeling = findViewById<Spinner>(R.id.spinnerFeeling)
        val spinnerPrice = findViewById<Spinner>(R.id.spinnerPrice)

        val btnSave = findViewById<Button>(R.id.btnSaveShop)

        btnSave.setOnClickListener {

            // 未選択チェック（0番目は「〜は？」）
            if (
                spinnerType.selectedItemPosition == 0 ||
                spinnerConcept.selectedItemPosition == 0 ||
                spinnerFeeling.selectedItemPosition == 0 ||
                spinnerPrice.selectedItemPosition == 0
            ) {
                Toast.makeText(this, "すべて選択してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val shopData = hashMapOf(
                "name" to editName.text.toString(),
                "address" to editAddress.text.toString(),
                "type" to spinnerType.selectedItem.toString(),
                "concept" to spinnerConcept.selectedItem.toString(),
                "feeling" to spinnerFeeling.selectedItem.toString(),
                "priceRange" to spinnerPrice.selectedItemPosition,
                "time" to editTime.text.toString(),
                "image" to editImage.text.toString(),
                "menu" to editMenu.text.toString()
            )

            db.collection("shop")
                .add(shopData)
                .addOnSuccessListener {
                    Toast.makeText(this, "お店を追加しました", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "追加に失敗しました", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
