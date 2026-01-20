package com.example.bmitracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import android.content.Intent

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Views
        val etWeight = findViewById<EditText>(R.id.etWeight)
        val etHeight = findViewById<EditText>(R.id.etHeight)
        val spGender = findViewById<Spinner>(R.id.spGender)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)

        // Spinner data
        val list = arrayOf("Male", "Female", "Other")
        spGender.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            list
        )

        // Button click
        btnCalculate.setOnClickListener {

            // Get input
            val weight = etWeight.text.toString().toDouble()
            val height = etHeight.text.toString().toDouble() / 100

            // BMI formula
            val bmi = weight / (height * height)

            // Category
            val status = when {
                bmi < 18.5 -> "Underweight"
                bmi < 24.9 -> "Normal"
                bmi < 29.9 -> "Overweight"
                else -> "Obese"
            }

            // Send to ResultActivity
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("bmi", bmi)
            intent.putExtra("status", status)
            startActivity(intent)
        }
    }
}
