package com.usfoouad.mypaper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_msg.*

class MSG : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msg)

        titleTextView.text = intent.extras.getString("title")
        timeTextView.text = intent.extras.getString("time")
        msgTextView.text = intent.extras.getString("msg")


    }
}