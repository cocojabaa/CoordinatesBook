package com.example.coordinatebook.presentation

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.coordinatebook.R
import com.example.coordinatebook.databinding.ActivityWorldBinding

class WorldActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorldBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorldBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = intent.getStringExtra("worldName")

    }


}