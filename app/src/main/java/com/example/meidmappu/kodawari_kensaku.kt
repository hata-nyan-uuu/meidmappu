package com.example.meidmappu

import android.util.Log
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.AdapterView


class kodawari_kensaku : AppCompatActivity() {

    private fun updateSpinner(spinner: Spinner, arrayId: Int) {
        val adapter = ArrayAdapter.createFromResource(
            this,
            arrayId,
            R.layout.spinner_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kodawari_kensaku)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Spinner 初期化（文字サイズ反映）
        setupSpinner(R.id.mise_type, R.array.mise_type)
        setupSpinner(R.id.conseputo2, R.array.conseputo2)
        setupSpinner(R.id.kakaku, R.array.kakaku)
        setupSpinner(R.id.feeling, R.array.feeling)

        val miseTypeSpinner = findViewById<Spinner>(R.id.mise_type)
        val conceptSpinner = findViewById<Spinner>(R.id.conseputo2)
        val feelingSpinner = findViewById<Spinner>(R.id.feeling)

        miseTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        1 -> { // メイドカフェ
                            updateSpinner(conceptSpinner, R.array.meid_conseputo)
                            updateSpinner(feelingSpinner, R.array.meid_feeling)
                        }
                        2 -> { // コンカフェ
                            updateSpinner(conceptSpinner, R.array.conseputo2)
                            updateSpinner(feelingSpinner, R.array.concafe_feeling)
                        }
                        else -> {
                            // 「お店のジャンルは？」のとき
                            updateSpinner(conceptSpinner, R.array.conseputo2)
                            updateSpinner(feelingSpinner, R.array.feeling)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }


        // 戻る
        findViewById<ImageButton>(R.id.backbtn2).setOnClickListener {
            finish()
        }

        // 検索
        findViewById<ImageButton>(R.id.kensaku).setOnClickListener {
            val type = getSpinnerValue(findViewById(R.id.mise_type))
            val priceStr = getSpinnerValue(findViewById(R.id.kakaku))
            val concept = getSpinnerValue(findViewById(R.id.conseputo2))
            val feeling = getSpinnerValue(findViewById(R.id.feeling))

            val priceRange = priceStr
                ?.replace("～", "")
                ?.replace(",", "")
                ?.replace("円", "")
                ?.toLongOrNull()

            Log.d(
                "DEBUG",
                "検索条件: type=$type priceRange=$priceRange concept=$concept feeling=$feeling"
            )

            val intent = Intent(this, KodawariSearchResult::class.java).apply {
                putExtra("type", type)
                putExtra("priceRange", priceRange)
                putExtra("concept", concept)
                putExtra("feeling", feeling)
            }
            startActivity(intent)
        }
    }

    // Spinner 共通設定
    private fun setupSpinner(spinnerId: Int, arrayId: Int) {
        val spinner = findViewById<Spinner>(spinnerId)
        val adapter = ArrayAdapter.createFromResource(
            this,
            arrayId,
            R.layout.spinner_item
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.adapter = adapter
    }

    // Spinner の先頭項目は未選択扱い
    private fun getSpinnerValue(spinner: Spinner): String? {
        return if (spinner.selectedItemPosition == 0) {
            null
        } else {
            spinner.selectedItem.toString()
        }
    }
}
