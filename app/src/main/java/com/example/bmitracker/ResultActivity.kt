package com.example.bmitracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val bmi = intent.getDoubleExtra("bmi", 0.0)
        val status = intent.getStringExtra("status")

        val tvBMI = findViewById<TextView>(R.id.tvBMI)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val tvMessage = findViewById<TextView>(R.id.tvMessage)
        val btnRecalculate = findViewById<Button>(R.id.btnRecalculate)

        tvBMI.text = String.format("%.2f", bmi)
        tvStatus.text = status

        when(status) {
            "Underweight" -> tvMessage.text = "Eat well & stay strong 💙"
            "Normal" -> tvMessage.text = "You're healthy! Keep it up 💪"
            "Overweight" -> tvMessage.text = "Time to move more 🏃"
            "Obese" -> tvMessage.text = "Start small, stay consistent 🔥"
        }

        // RE-CALCULATE
        btnRecalculate.setOnClickListener {
            startActivity(Intent(this, DetailsActivity::class.java))
            finish()
        }
    }
}
