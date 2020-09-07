package com.decideforme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_decide.*

class DecideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decide)

        btnDecide.setOnClickListener {
            Toast.makeText(this,"Currently a work in progress, this will eventually decide for you", Toast.LENGTH_LONG).show()
        }

        btnToListMenu.setOnClickListener {
            Toast.makeText(this,"Currently a work in progress, this will take you to the list of items", Toast.LENGTH_LONG).show()
        }
    }
}