package com.example.messanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        already_accunt.setOnClickListener {
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
            finish()
        }
        register_btn.setOnClickListener {
            performregister()
        }
    }
    private fun performregister(){
        val username= username_registration.text.toString()
        val email = email_registration.text.toString()
        val password= registration_password.text.toString()
        val etusername:EditText = findViewById(R.id.username_registration)
        val etemail:EditText=findViewById(R.id.email_registration)
        val etpassword:EditText= findViewById(R.id.registration_password)
        if (email.isEmpty()){
            etemail.error="Email Required"
            etemail.setBackgroundResource(R.drawable.input_field_error)
        }else if (password.isEmpty()){
            etpassword.error="Required Password"
            etpassword.setBackgroundResource(R.drawable.input_field_error)
        }else if (username.isEmpty()){
            etusername.error="Required Username"
            etusername.setBackgroundResource(R.drawable.input_field_error)
        }else{
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener
                    Log.d("Register","Account created successfully.")
                    Toast.makeText(this,"Account created successfully",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this,"Failed to create Account ${it.message}",Toast.LENGTH_SHORT).show()
                }
        }
    }

}
