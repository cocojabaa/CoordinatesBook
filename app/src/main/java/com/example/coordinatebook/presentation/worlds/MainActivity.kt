package com.example.coordinatebook.presentation.worlds

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coordinatebook.R
import com.example.coordinatebook.data.coordinates.CoordinatesRepositoryImpl
import com.example.coordinatebook.data.worlds.WorldsRepositoryImpl
import com.example.coordinatebook.databinding.ActivityMainBinding
import com.example.coordinatebook.domain.CoordinatesRepository
import com.example.coordinatebook.domain.WorldsRepository
import com.example.coordinatebook.domain.models.WorldInfo
import com.example.coordinatebook.domain.usecases.coordinates.DeleteAllCoordinatesByIdUseCase
import com.example.coordinatebook.domain.usecases.worlds.AddWorldUseCase
import com.example.coordinatebook.domain.usecases.worlds.DeleteWorldUseCase
import com.example.coordinatebook.domain.usecases.worlds.GetAllWorldsUseCase
import com.example.coordinatebook.presentation.coordinates.CoordinatesActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), WorldClickListener {
    val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val worldsRepository: WorldsRepository by lazy { WorldsRepositoryImpl(this) }
    val coordinatesRepository: CoordinatesRepository by lazy { CoordinatesRepositoryImpl(this) }

    val getAllWorldsUseCase: GetAllWorldsUseCase by lazy { GetAllWorldsUseCase(worldsRepository) }
    val addWorldUseCase: AddWorldUseCase by lazy { AddWorldUseCase(worldsRepository) }
    val deleteWorldUseCase: DeleteWorldUseCase by lazy { DeleteWorldUseCase(worldsRepository) }

    val deleteAllCoordinatesByIdUseCase: DeleteAllCoordinatesByIdUseCase by lazy { DeleteAllCoordinatesByIdUseCase(coordinatesRepository) }

    lateinit var worldsAdapter: WorldsRecyclerAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecyclerView()

        binding.showCreateWorldDialogButton.setOnClickListener {
            showCreateWorldDialog()
        }

    }

    private fun initRecyclerView() {
        CoroutineScope(Dispatchers.IO).launch {
            val worlds = async {getAllWorldsUseCase.execute()}.await()
            runOnUiThread {
                worldsAdapter = WorldsRecyclerAdapter(worlds, this@MainActivity)
                binding.recyclerView.adapter = worldsAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

            }
        }

    }

    override fun onWorldClick(worldInfo: WorldInfo) {
        val intent = Intent(this, CoordinatesActivity::class.java)
        intent.putExtra("worldId", worldInfo.id)
        intent.putExtra("worldName", worldInfo.name)
        Log.i("My", "WORLD ID ON CLICK: ${worldInfo.id}")
        startActivity(intent)
    }

    override fun onWorldLongClick(worldInfo: WorldInfo) {
        showDeleteWorldDialog(worldInfo)
    }

    fun showCreateWorldDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.create_world_dialog, null)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val doneButton = dialogBinding.findViewById<Button>(R.id.doneButton)
        doneButton.setOnClickListener {
            val nameView = dialogBinding.findViewById<TextInputEditText>(R.id.inputWorldName)
            val descriptionView = dialogBinding.findViewById<TextInputEditText>(R.id.inputWorldDescription)
            val nameText = nameView.text.toString()
            val descriptionText = descriptionView.text.toString()

            if (nameText.isEmpty()) {
                nameView.error = "Введите название мира"
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                if (!isWorldUnique(nameText)) {
                    runOnUiThread {
                        nameView.error = "Мир с таким именем уже есть"
                    }
                    return@launch
                }
                var worldInfo = WorldInfo(
                    name = nameText,
                    description = descriptionText
                )
                val newWorldId = async { addWorldUseCase.execute(worldInfo) }.await()
                runOnUiThread {
                    if (newWorldId != null) {
                        worldInfo.id = newWorldId
                        worldsAdapter.addWorld(worldInfo)
                        Log.i("My", "WORLD ID AFTER ADAPTER ${worldInfo.id}")
                        dialog.dismiss()
                    } else Toast.makeText(this@MainActivity, "Мир не записался (worldId = null)", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }

    fun showDeleteWorldDialog(worldInfo: WorldInfo) {
        val dialogBinding = layoutInflater.inflate(R.layout.delete_world_dialog, null)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val worldNameText = dialog.findViewById<TextView>(R.id.worldNameText)
        worldNameText.text = worldInfo.name

        val deleteButton = dialog.findViewById<Button>(R.id.deleteWorldButton)
        deleteButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                worldInfo.id?.let { id -> deleteAllCoordinatesByIdUseCase.execute(id) }
                val result = async {deleteWorldUseCase.execute(worldInfo.name)}.await()
                runOnUiThread {
                    if (result) worldsAdapter.deleteWorld(worldInfo)
                    else Toast.makeText(this@MainActivity, "Мир не удалился(", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }

    suspend fun isWorldUnique(worldName: String): Boolean {
        val worldsList = getAllWorldsUseCase.execute()
        worldsList.forEach {
            if (it.name == worldName) return false
        }
        return true
    }
}