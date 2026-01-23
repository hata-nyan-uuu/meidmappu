package com.example.meidmappu

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge
import com.google.firebase.firestore.FirebaseFirestore

class AdminAddShopActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private fun getSelectedFeelings(): List<String> {
        val list = mutableListOf<String>()

        if (findViewById<CheckBox>(R.id.feeling).isChecked)
            list.add("ごはんたべたい")

        if (findViewById<CheckBox>(R.id.feeling1).isChecked)
            list.add("ゆっくりしたい")

        if (findViewById<CheckBox>(R.id.feeling2).isChecked)
            list.add("だれかとはなしたい")

        if (findViewById<CheckBox>(R.id.feeling3).isChecked)
            list.add("お酒をのみたい")

        return list
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  edge-to-edge 有効化
        enableEdgeToEdge()

        setContentView(R.layout.activity_admin_add_shop)

        //Insets 設定（systemBars + ime）
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


        // EditText
        val editName = findViewById<EditText>(R.id.editName)
        val editAddress = findViewById<EditText>(R.id.editAddress)
        val editTime = findViewById<EditText>(R.id.editTime)
        val editImage = findViewById<EditText>(R.id.editImage)
        val editMenu = findViewById<EditText>(R.id.editMenu)
        val editWebsite = findViewById<EditText>(R.id.edit_website)
        val editInstagram = findViewById<EditText>(R.id.edit_instagram)
        val editX = findViewById<EditText>(R.id.edit_x)
        val editTikTok = findViewById<EditText>(R.id.edit_tiktok)

        // Spinner
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)
        val spinnerConcept = findViewById<Spinner>(R.id.spinnerConcept)
        val spinnerPrice = findViewById<Spinner>(R.id.spinnerPrice)

        val btnSave = findViewById<Button>(R.id.btnSaveShop)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {

            val selectedFeelings = getSelectedFeelings()

            if (selectedFeelings.isEmpty()) {
                Toast.makeText(
                    this, "気分を1つ以上選んでください",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (
                spinnerType.selectedItemPosition == 0 ||
                spinnerConcept.selectedItemPosition == 0 ||
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

            val shopData = hashMapOf(
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
                "feeling" to selectedFeelings
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
