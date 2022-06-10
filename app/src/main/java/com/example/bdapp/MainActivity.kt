package com.example.bdapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bdapp.databinding.ActivityMainBinding
import androidx.navigation.fragment.findNavController as find

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide() // Oculta la barra de acciones
    }
}