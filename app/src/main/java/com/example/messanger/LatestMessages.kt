package com.example.messanger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.messanger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LatestMessages : AppCompatActivity() {

    companion object{
        var currentuser: User?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        fetchcurrentuser()
      verifyuserloggedin()
        }
    private fun fetchcurrentuser(){
       val  uid=FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                currentuser=p0.getValue(User::class.java)

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun verifyuserloggedin(){
    val uid = FirebaseAuth.getInstance().uid
    if (uid== null){
        val intent = Intent(this,MainActivity::class.java)
        intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)}
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
         when(item?.itemId){
             R.id.menu_new_message ->{
                 val intent= Intent(this,NewMessage::class.java)
                 startActivity(intent)
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

