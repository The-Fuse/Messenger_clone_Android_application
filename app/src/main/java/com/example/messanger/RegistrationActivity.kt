package com.example.messanger

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class MainActivity : AppCompatActivity() {
    var selectedPhotoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        already_accunt.setOnClickListener {
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish()
        }
        register_btn.setOnClickListener {
            performregister()
        }
        selectphoto_button_register.setOnClickListener {
            val intent =Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==0 && resultCode==Activity.RESULT_OK && data!= null){
            //perform some
            selectedPhotoUri =data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
           selectphoto_imageview.setImageBitmap(bitmap)
            selectphoto_button_register.alpha = 0f

        }
    }
    private fun performregister(){
        val username= username_registration.text.toString()
        val email = email_registration.text.toString()
        val password= registration_password.text.toString()
        val etusername:EditText = findViewById(R.id.username_registration)
        val etemail:EditText=findViewById(R.id.email_registration)
        val etpassword:EditText= findViewById(R.id.registration_password)
        if (username.isEmpty()){
            etusername.error="Username Required"
            etusername.setBackgroundResource(R.drawable.input_field_error)
        }else if (email.isEmpty()){
            etemail.error="Required Email"
            etemail.setBackgroundResource(R.drawable.input_field_error)
        }else if (password.isEmpty()){
            etpassword.error="Required Password"
            etpassword.setBackgroundResource(R.drawable.input_field_error)
        }else{
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener

                    Log.d("Register","Account created successfully.")
                    uploadimagetoFirebaseStorage()
                    Toast.makeText(this,"Account created successfully",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed to create Account ${it.message}",Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun uploadimagetoFirebaseStorage(){
        if(selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref=   FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("MainActivity","Succesfully uploaded image:${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                   saveusertodatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("MainActivity","${it.message}")
            }
    }
    private fun saveusertodatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref=FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user =User(uid,username_registration.text.toString(),profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("MainActivity","Finally we saved user to firebase database.")
                val intent = Intent(this,LatestMessages::class.java)
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {

            }
    }

}
class User(val uid:String,val username:String, val profileImageUrl:String){
    constructor(): this("","","")
}
