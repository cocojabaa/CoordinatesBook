package com.example.coordinatebook.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coordinatebook.databinding.ActivityWorldBinding

class CoordinatesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorldBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorldBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = intent.getStringExtra("worldName")

    }


}