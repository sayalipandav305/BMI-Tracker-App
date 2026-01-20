package com.example.bmitracker

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var googleClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // Views
        val email = findViewById<EditText>(R.id.etEmail)
        val pass = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val btnGoogle = findViewById<Button>(R.id.btnGoogle)
        val btnPhone = findViewById<Button>(R.id.btnPhone)
        val tvForgot = findViewById<TextView>(R.id.tvForgot)

        // GOOGLE CONFIG
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleClient = GoogleSignIn.getClient(this, gso)

        // EMAIL LOGIN
        btnLogin.setOnClickListener {

            if(email.text.isEmpty() || pass.text.isEmpty()){
                toast("Fill all fields")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(
                email.text.toString(),
                pass.text.toString()
            ).addOnSuccessListener {

                toast("Login successful!")
                startActivity(Intent(this, DetailsActivity::class.java))
                finish()

            }.addOnFailureListener {
                toast(it.message!!)
            }
        }

        // SIGNUP SCREEN
        btnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // GOOGLE LOGIN
        btnGoogle.setOnClickListener {
            startActivityForResult(googleClient.signInIntent, 101)
        }

        // PHONE LOGIN
        btnPhone.setOnClickListener {
            startActivity(Intent(this, PhoneAuthActivity::class.java))
        }

        // FORGOT PASSWORD
        tvForgot.setOnClickListener {

            if(email.text.isEmpty()){
                toast("Enter email first")
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email.text.toString())
            toast("Reset mail sent!")
        }
    }

    // GOOGLE RESULT
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 101){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                toast("Google sign-in failed")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnSuccessListener {

                toast("Google login success!")
                startActivity(Intent(this, DetailsActivity::class.java))
                finish()

            }.addOnFailureListener {
                toast(it.message!!)
            }
    }

    private fun toast(msg: String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }
}
