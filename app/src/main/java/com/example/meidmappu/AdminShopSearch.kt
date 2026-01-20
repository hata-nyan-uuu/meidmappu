package com.example.meidmappu

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminShopSearch : AppCompatActivity() {
    private val allShopList = mutableListOf<ShopFirestore>()
    private val db = FirebaseFirestore.getInstance()
    private val shopList = mutableListOf<ShopFirestore>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_shop_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val editSearch = findViewById<EditText>(R.id.editSearch)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = ShopAdapter(shopList) { shop ->

            val intent = Intent(this, AdminEditShopActivity::class.java)
            intent.putExtra("shopId", shop.id)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        loadShops(adapter)
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterShops(s.toString(), adapter)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })



    }

    private fun filterShops(keyword: String, adapter: RecyclerView.Adapter<*>) {
        shopList.clear()

        if (keyword.isBlank()) {
            shopList.addAll(allShopList)
        } else {
            shopList.addAll(
                allShopList.filter {
                    it.name.contains(keyword, ignoreCase = true)
                }
            )
        }
        adapter.notifyDataSetChanged()
    }


    private fun loadShops(adapter: RecyclerView.Adapter<*>) {
        db.collection("shop")
            .get()
            .addOnSuccessListener { result ->
                allShopList.clear()
                shopList.clear()

                for (doc in result) {
                    val shop = ShopFirestore(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        image = doc.getString("image")
                    )
                    allShopList.add(shop)
                }

                shopList.addAll(allShopList)
                adapter.notifyDataSetChanged()
            }

        findViewById<Button>(R.id.btnBack2).setOnClickListener {
            finish()
        }
    }

}
