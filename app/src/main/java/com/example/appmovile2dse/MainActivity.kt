package com.example.appmovile2dse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ejemplo de uso de Kotlin con Android
        val welcomeText: TextView = findViewById(R.id.welcomeText)
        val clickButton: Button = findViewById(R.id.clickButton)
        
        var clickCount = 0
        
        clickButton.setOnClickListener {
            clickCount++
            welcomeText.text = "Has hecho clic $clickCount veces!"
        }
    }
}
