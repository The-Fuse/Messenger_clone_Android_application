package com.example.messanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class Chatlog : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlog)
        supportActionBar?.title="Chat Log"

    }
}
class Chatitem: Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

    override fun getLayout(): Int {

    }
}