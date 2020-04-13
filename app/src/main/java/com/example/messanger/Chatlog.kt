package com.example.messanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Chatlog : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlog)
        supportActionBar?.title="Chat Log"

    }
}
