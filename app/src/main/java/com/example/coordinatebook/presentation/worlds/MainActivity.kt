package com.example.coordinatebook.presentation.worlds

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
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
import com.example.coordinatebook.domain.usecases.worlds.EditWorldUseCase
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
    val editWorldUseCase: EditWorldUseCase by lazy { EditWorldUseCase(worldsRepository) }

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
        startActivity(intent)
    }

    override fun onWorldLongClick(worldInfo: WorldInfo) {
        val settingDialogBinding = layoutInflater.inflate(R.layout.setting_dialog, null)
        val settingDialog = Dialog(this)
        settingDialog.setContentView(settingDialogBinding)
        settingDialog.setCancelable(true)
        settingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val deleteButton = settingDialogBinding.findViewById<Button>(R.id.deleteButton)
        val editButton = settingDialogBinding.findViewById<Button>(R.id.editButton)

        deleteButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val deleteSuccess = async {deleteWorldUseCase.execute(worldInfo.id)}.await()
                if (deleteSuccess) worldInfo.id?.let { id -> deleteAllCoordinatesByIdUseCase.execute(id) }
                runOnUiThread {
                    if (deleteSuccess) {
                        worldsAdapter.deleteWorld(worldInfo)
                        settingDialog.dismiss()
                    }
                }
            }
        }
        editButton.setOnClickListener {
            showEditWorldDialog(worldInfo)
            settingDialog.dismiss()
        }
        settingDialog.show()
    }

    fun showCreateWorldDialog() {
        val createDialogBinding = layoutInflater.inflate(R.layout.create_world_dialog, null)
        val createDialog = Dialog(this)
        createDialog.setContentView(createDialogBinding)
        createDialog.setCancelable(true)
        createDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val doneButton = createDialogBinding.findViewById<Button>(R.id.doneButton)
        doneButton.setOnClickListener {
            val nameView = createDialogBinding.findViewById<TextInputEditText>(R.id.inputWorldName)
            val descriptionView = createDialogBinding.findViewById<TextInputEditText>(R.id.inputWorldDescription)
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
                        createDialog.dismiss()
                    } else Toast.makeText(this@MainActivity, "Мир не записался (worldId = null)", Toast.LENGTH_SHORT).show()
                }
            }
        }
        createDialog.show()
    }

    fun showEditWorldDialog(worldInfo: WorldInfo) {
        val editDialogBinding = layoutInflater.inflate(R.layout.edit_world_dialog, null)
        val editDialog = Dialog(this)
        editDialog.setContentView(editDialogBinding)
        editDialog.setCancelable(true)
        editDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val editNameView = editDialogBinding.findViewById<TextInputEditText>(R.id.editWorldName)
        val editDescriptionView = editDialogBinding.findViewById<TextInputEditText>(R.id.editWorldDescription)
        val acceptButton = editDialogBinding.findViewById<Button>(R.id.acceptChangesWorldButton)

        acceptButton.setOnClickListener {
            val newWorldInfo = WorldInfo(
                worldInfo.id,
                editNameView.text.toString(),
                editDescriptionView.text.toString()
            )
            CoroutineScope(Dispatchers.IO).launch {
                val editSuccess = async{ editWorldUseCase.execute(newWorldInfo) }.await()
                if (editSuccess) runOnUiThread {
                    worldsAdapter.deleteWorld(worldInfo)
                    worldsAdapter.addWorld(newWorldInfo)
                    Toast.makeText(this@MainActivity, "Мир изменен", Toast.LENGTH_SHORT).show()
                } else Toast.makeText(this@MainActivity, "Якась ошибка", Toast.LENGTH_SHORT).show()
                editDialog.dismiss()
            }
        }

        editNameView.setText(worldInfo.name)
        editDescriptionView.setText(worldInfo.description)

        editDialog.show()
    }

    suspend fun isWorldUnique(worldName: String): Boolean {
        val worldsList = getAllWorldsUseCase.execute()
        worldsList.forEach {
            if (it.name == worldName) return false
        }
        return true
    }
}