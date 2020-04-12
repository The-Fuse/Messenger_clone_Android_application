package com.example.messanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

class LatestMessages : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        
      verifyuserloggedin()
        }

    private fun verifyuserloggedin(){
    val uid = FirebaseAuth.getInstance().uid
    if (uid== null){
        val intent = Intent(this,MainActivity::class.java)
        intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)}
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when(item?.itemId){
             R.id.menu_new_message ->{

             }
             R.id.menu_sign_out ->{
                 FirebaseAuth.getInstance().signOut()
                 val intent = Intent(this,MainActivity::class.java)
                 intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                 startActivity(intent)
             }
         }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nac_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}

