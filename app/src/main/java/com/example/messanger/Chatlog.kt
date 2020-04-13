package com.example.messanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.messanger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chatlog.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class Chatlog : AppCompatActivity() {
    val adapter= GroupAdapter<ViewHolder>()
    val touser :User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatlog)
        val touser = intent.getParcelableExtra<User>(NewMessage.USER_KEY)
         recyclerView_newmessages_chatlog.adapter=adapter
        supportActionBar?.title=touser?.username
        listenformessages()
        send_btn_chatlog.setOnClickListener {
            performsendmessage()
        }
    }

    private fun listenformessages(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages")
        ref.addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatmessage=p0.getValue(Chatmessage::class.java)
                if (chatmessage!=null){
                    Log.d("Main",chatmessage.text)
                }
                if (chatmessage.id==FirebaseAuth.getInstance().uid){
                    val currentuser = LatestMessages.currentuser
                    adapter.add(ChatFromitem(chatmessage.text,currentuser!!))

                }else{
                 adapter.add(Chattoitem(chatmessage.text,touser!!))

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }
    class Chatmessage(val id:String,val text: String,val fromId:String,val toId:String,val timestamp:Long){
        constructor(): this("","","","",-1)
    }
    private fun performsendmessage(){
       val text= entermessage_edittext.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessage.USER_KEY)
        if (fromId==null) return
        val toId= user.uid
        val reference=FirebaseDatabase.getInstance().getReference("/messages").push()
        val chatmessage = Chatmessage(reference.key!!,text,fromId,toId,System.currentTimeMillis()/1000)

        reference.setValue(chatmessage)
            .addOnSuccessListener {

            }

    }
}
class ChatFromitem(val text:String,val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
       viewHolder.itemView.textView_from_row.text=text
        val uri = user.profileImageUrl
        val targetimageview= viewHolder.itemView.imageView_from_user
        Picasso.get().load(uri).into(targetimageview)
    }

    override fun getLayout(): Int {
         return R.layout.chat_from_row
    }
}
class Chattoitem(val text:String,val user:User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text= text
        val uri = user.profileImageUrl
        val targetimageview= viewHolder.itemView.imageView_to_user
        Picasso.get().load(uri).into(targetimageview)

    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}