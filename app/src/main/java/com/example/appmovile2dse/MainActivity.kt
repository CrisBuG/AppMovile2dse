package com.example.appmovile2dse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appmovile2dse.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ejemplo de uso de Kotlin con Android y ViewBinding
        var clickCount = 0
        
        binding.clickButton.setOnClickListener {
            clickCount++
            binding.welcomeText.text = "Has hecho clic $clickCount veces!"
        }
    }
}
