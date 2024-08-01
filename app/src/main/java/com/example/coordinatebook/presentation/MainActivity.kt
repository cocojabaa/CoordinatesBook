package com.example.coordinatebook.presentation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coordinatebook.R
import com.example.coordinatebook.data.WorldsDatabaseApiImpl
import com.example.coordinatebook.databinding.ActivityMainBinding
import com.example.coordinatebook.domain.WorldsDatabaseApi
import com.example.coordinatebook.domain.models.WorldInfo
import com.example.coordinatebook.domain.usecases.AddWorldUseCase
import com.example.coordinatebook.domain.usecases.DeleteWorldUseCase
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), WorldClickListener {
    val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val worldsDatabaseApi: WorldsDatabaseApi by lazy { WorldsDatabaseApiImpl(this) }

    val addWorldUseCase: AddWorldUseCase by lazy { AddWorldUseCase() }
    val deleteWorldUseCase: DeleteWorldUseCase by lazy { DeleteWorldUseCase() }

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
        CoroutineScope(Dispatchers.IO).launch {
            val worlds = async {worldsDatabaseApi.getAllWorlds()}.await()
            runOnUiThread {
                worldsAdapter = WorldsRecyclerAdapter(worlds, this@MainActivity)
                binding.recyclerView.adapter = worldsAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

            }
        }

    }

    override fun onWorldClick(worldInfo: WorldInfo) {
        // TODO
    }

    override fun onWorldLongClick(worldInfo: WorldInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = async {deleteWorldUseCase.execute(worldInfo, worldsDatabaseApi)}.await()
            runOnUiThread {
                if (result) worldsAdapter.deleteWorld(worldInfo)
                else Toast.makeText(this@MainActivity, "Мир не удалился", Toast.LENGTH_SHORT).show()
            }
        }
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
            if (name.text.isNullOrEmpty()) {
                name.error = "Введите название мира"
                return@setOnClickListener
            }
            val worldInfo = WorldInfo(
                name = name.text.toString(),
                description = description.text.toString()
            )
            CoroutineScope(Dispatchers.IO).launch {
                val result = async {addWorldUseCase.execute(worldInfo, worldsDatabaseApi)}.await()
                runOnUiThread {
                    if (result) {
                        worldsAdapter.addWorld(worldInfo)
                        Toast.makeText(this@MainActivity, "ОК", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else Toast.makeText(this@MainActivity, "Мир не записался", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }
}