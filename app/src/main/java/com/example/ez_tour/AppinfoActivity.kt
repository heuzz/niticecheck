package com.example.ez_tour

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class AppinfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appinfo)

        val btn_back2 = findViewById<Button>(R.id.btn_back2) as ImageButton

        btn_back2.setOnClickListener {
            finish()


        }
    }
}