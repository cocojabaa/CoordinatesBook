package com.example.coordinatebook.presentation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coordinatebook.R
import com.example.coordinatebook.data.WorldsDatabaseApiImpl
import com.example.coordinatebook.databinding.ActivityMainBinding
import com.example.coordinatebook.domain.WorldsDatabaseApi
import com.example.coordinatebook.domain.models.WorldInfo
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {
    val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val worldsDatabaseApi: WorldsDatabaseApi by lazy { WorldsDatabaseApiImpl(this) }

    lateinit var worldsAdapter: WorldsRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initWorldsRecycler()

        binding.createWorldButton.setOnClickListener {
            showCreateWorldDialog()
        }
    }

    private fun initWorldsRecycler() {
        Thread {
            val worlds = worldsDatabaseApi.getAllWorlds()
            worldsAdapter = WorldsRecyclerAdapter(worlds)
            binding.recyclerView.adapter = worldsAdapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
        }.start()
    }

    fun showCreateWorldDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.create_world_dialog, null)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val doneButton = dialogBinding.findViewById<Button>(R.id.doneButton)
        doneButton.setOnClickListener {
            val name = dialogBinding.findViewById<TextInputEditText>(R.id.inputWorldName)
            val description = dialogBinding.findViewById<TextInputEditText>(R.id.inputWorldDescription)
            val worldInfo = WorldInfo(
                name = name.text.toString(),
                description = description.text.toString()
            )
            Thread{
                val response = worldsDatabaseApi.addWorld(worldInfo)
            }.start()
            worldsAdapter.addWorld(worldInfo)
            dialog.dismiss()
        }

        dialog.show()
    }
}