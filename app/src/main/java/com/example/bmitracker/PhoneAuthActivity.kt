package com.example.bmitracker

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class PhoneAuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        auth = FirebaseAuth.getInstance()

        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etOtp = findViewById<EditText>(R.id.etOtp)
        val btnSend = findViewById<Button>(R.id.btnSend)
        val btnVerify = findViewById<Button>(R.id.btnVerify)

        // SEND OTP
        btnSend.setOnClickListener {
            val phone = etPhone.text.toString().trim()

            if (phone.length < 10) {
                Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sendOtp("+91$phone") // India code
        }

        // VERIFY OTP
        btnVerify.setOnClickListener {
            val otp = etOtp.text.toString().trim()

            if (otp.length < 6) {
                Toast.makeText(this, "Enter valid OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            signInWithPhone(credential)
        }
    }

    // SEND OTP FUNCTION
    private fun sendOtp(number: String) {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // CALLBACKS
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhone(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@PhoneAuthActivity, e.message, Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            this@PhoneAuthActivity.verificationId = verificationId
            Toast.makeText(this@PhoneAuthActivity, "OTP Sent ✅", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithPhone(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                Toast.makeText(this, "Login Successful 🎉", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DetailsActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }
}
