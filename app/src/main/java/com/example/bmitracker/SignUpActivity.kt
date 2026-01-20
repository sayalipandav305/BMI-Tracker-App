package com.example.bmitracker

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.etEmail)
        val pass = findViewById<EditText>(R.id.etPassword)
        val confirm = findViewById<EditText>(R.id.etConfirm)
        val btn = findViewById<Button>(R.id.btnSignup)
        val login = findViewById<TextView>(R.id.tvLogin)

        btn.setOnClickListener {

            if(email.text.isEmpty() || pass.text.isEmpty() || confirm.text.isEmpty()){
                toast("Fill all fields")
                return@setOnClickListener
            }

            if(pass.text.length < 6){
                toast("Password must be 6+ chars")
                return@setOnClickListener
            }

            if(pass.text.toString() != confirm.text.toString()){
                toast("Passwords do not match")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(
                email.text.toString(),
                pass.text.toString()
            ).addOnSuccessListener {

                toast("Account created 🎉")
                startActivity(Intent(this, DetailsActivity::class.java))
                finish()

            }.addOnFailureListener {
                toast(it.message!!)
            }
        }

        login.setOnClickListener {
            finish() // back to login
        }
    }

    private fun toast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }
}
