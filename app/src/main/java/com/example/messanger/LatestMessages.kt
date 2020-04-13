package com.example.messanger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.messanger.models.Chatmessage
import com.example.messanger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessages : AppCompatActivity() {

    companion object{
        var currentuser: User?=null
    }
    val adapter= GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        recyclerview_latest_messages.adapter=adapter
        listenForlatestMessages()
        fetchcurrentuser()
        verifyuserloggedin()
        }
    val latestMessagesMap = HashMap<String,Chatmessage>()
    private fun refreshrecyclerview(){
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun listenForlatestMessages(){
        val fromId= FirebaseAuth.getInstance().uid
        val ref= FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage= p0.getValue(Chatmessage::class.java) ?: return
                latestMessagesMap[p0.key!!]= chatMessage
                refreshrecyclerview()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(Chatmessage::class.java) ?: return
                latestMessagesMap[p0.key!!]= chatMessage
                refreshrecyclerview()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })



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
class LatestMessageRow(val chatMessage: Chatmessage): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_latest_message
    }
}

