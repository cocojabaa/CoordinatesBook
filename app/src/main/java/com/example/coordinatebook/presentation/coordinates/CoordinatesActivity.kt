package com.example.coordinatebook.presentation.coordinates

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coordinatebook.R
import com.example.coordinatebook.data.coordinates.CoordinatesRepositoryImpl
import com.example.coordinatebook.databinding.ActivityWorldBinding
import com.example.coordinatebook.domain.CoordinatesRepository
import com.example.coordinatebook.domain.models.CoordinatesInfo
import com.example.coordinatebook.domain.models.Dimensions
import com.example.coordinatebook.domain.usecases.coordinates.AddCoordinatesUseCase
import com.example.coordinatebook.domain.usecases.coordinates.DeleteCoordinatesUseCase
import com.example.coordinatebook.domain.usecases.coordinates.GetCoordinatesByIdUseCase
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CoordinatesActivity : AppCompatActivity(), CoordinatesClickListener {
    lateinit var binding: ActivityWorldBinding

    lateinit var adapter: CoordinatesRecyclerAdapter

    var clipboard: ClipboardManager? = null

    val worldName: String by lazy { intent.getStringExtra("worldName").toString() }
    val worldId: Int by lazy { intent.getIntExtra("worldId", 0) }

    val coordinatesRepository: CoordinatesRepository by lazy { CoordinatesRepositoryImpl(this) }
    val getCoordinatesByIdUseCase: GetCoordinatesByIdUseCase by lazy { GetCoordinatesByIdUseCase(coordinatesRepository) }
    val addCoordinatesUseCase: AddCoordinatesUseCase by lazy { AddCoordinatesUseCase(coordinatesRepository) }
    val deleteCoordinatesUseCase: DeleteCoordinatesUseCase by lazy { DeleteCoordinatesUseCase(coordinatesRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorldBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = worldName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?

        initRecyclerView(worldId)

        binding.showAddCoordinatesDialogButton.setOnClickListener {
            showAddCoordinatesDialog()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return true
    }

    fun initRecyclerView(worldId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val coordinatesList = async{getCoordinatesByIdUseCase.execute(worldId)}.await()
            adapter = CoordinatesRecyclerAdapter(coordinatesList, this@CoordinatesActivity)
            binding.coordinatesRecycler.adapter = adapter
            binding.coordinatesRecycler.layoutManager = LinearLayoutManager(this@CoordinatesActivity)
        }
    }

    override fun onCoordinatesLongClick(coordinatesInfo: CoordinatesInfo) {
        showDeleteCoordinatesDialog(coordinatesInfo)
    }

    override fun onCoordinatesClick(coordinatesInfo: CoordinatesInfo) {
        var coordinatesText = ""
        if (coordinatesInfo.y == null) coordinatesText = "${coordinatesInfo.x} ${coordinatesInfo.z}"
        else coordinatesText = "${coordinatesInfo.x} ${coordinatesInfo.y} ${coordinatesInfo.z}"
        clipboard?.setPrimaryClip(ClipData.newPlainText("coords", coordinatesText))
        Toast.makeText(this, "Координаты скопированы!", Toast.LENGTH_SHORT).show()
    }

    fun showAddCoordinatesDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.add_coordinates_dialog, null)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val addButton = dialogBinding.findViewById<Button>(R.id.addCoordinatesButton)
        val pasteButton = dialogBinding.findViewById<Button>(R.id.pasteCoordinates)

        val inputXView = dialogBinding.findViewById<TextInputEditText>(R.id.inputX)
        val inputYView = dialogBinding.findViewById<TextInputEditText>(R.id.inputY)
        val inputZView = dialogBinding.findViewById<TextInputEditText>(R.id.inputZ)
        val inputDescriptionView = dialogBinding.findViewById<TextInputEditText>(R.id.inputCoordinatesDescription)

        val upperWorldRadio = dialogBinding.findViewById<RadioButton>(R.id.upperWorldRadio)
        val netherRadio = dialogBinding.findViewById<RadioButton>(R.id.netherRadio)
        val endRadio = dialogBinding.findViewById<RadioButton>(R.id.endRadio)


        addButton.setOnClickListener {
            val inputXText = inputXView.text.toString()
            val inputYText = inputYView.text.toString()
            val inputZText = inputZView.text.toString()
            val inputDescriptionText = inputDescriptionView.text.toString()

            if (inputXText.isEmpty()) {
                runOnUiThread{ inputXView.error = "Введите значение" }
                return@setOnClickListener
            }
            if (inputZText.isEmpty()) {
                runOnUiThread{ inputZView.error = "Введите значение" }
                return@setOnClickListener
            }
            var inputDimension: Dimensions = Dimensions.UpperWorld
            var yCoordinate: Int? = null

            if (inputYText.length != 0) yCoordinate = inputYText.toInt()
            if (upperWorldRadio.isChecked) inputDimension = Dimensions.UpperWorld
            if (netherRadio.isChecked) inputDimension = Dimensions.Nether
            if (endRadio.isChecked) inputDimension = Dimensions.End

            val coordinatesInfo = CoordinatesInfo(
                worldId = worldId,
                description = inputDescriptionText,
                dimension = inputDimension,
                x = inputXText.toInt(),
                y = yCoordinate,
                z = inputZText.toInt()
            )

            CoroutineScope(Dispatchers.IO).launch {
                val addCoordinatesSuccess = async { addCoordinatesUseCase.execute(coordinatesInfo) }.await()
                if (addCoordinatesSuccess) {
                    runOnUiThread {
                        adapter.addCoordinates(coordinatesInfo)
                        dialog.dismiss()
                    }
                }
            }
        }

        pasteButton.setOnClickListener {
            val clipData = clipboard?.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                val clipDataText = clipData.getItemAt(0).coerceToText(this).toString()
                if (isCoordinatesCorrect(clipDataText)) {
                    val coordinates = clipDataText.split(" ")

                    val inputXView = dialogBinding.findViewById<TextInputEditText>(R.id.inputX)
                    val inputYView = dialogBinding.findViewById<TextInputEditText>(R.id.inputY)
                    val inputZView = dialogBinding.findViewById<TextInputEditText>(R.id.inputZ)

                    when (coordinates.size) {
                        3 -> {
                            inputXView.setText(coordinates[0])
                            inputYView.setText(coordinates[1])
                            inputZView.setText(coordinates[2])
                        }
                        2 -> {
                            inputXView.setText(coordinates[0])
                            inputZView.setText(coordinates[1])
                        }
                    }
                } else Toast.makeText(this, "Неверный формат скопированного текста", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this, "Нет скопированного текста", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }

    fun showDeleteCoordinatesDialog(coordinatesInfo: CoordinatesInfo) {
        val dialogBinding = layoutInflater.inflate(R.layout.delete_coordinates_dialog, null)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val deleteButton = dialogBinding.findViewById<Button>(R.id.deleteCoordinatesButton)
        deleteButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val deleteSuccess = async {deleteCoordinatesUseCase.execute(coordinatesInfo)}.await()
                if (deleteSuccess) {
                    runOnUiThread {
                        adapter.deleteCoordinates(coordinatesInfo)
                        dialog.dismiss()
                    }
                }
            }
        }
        dialog.show()
    }

    fun isCoordinatesCorrect(clipDataText: String): Boolean {
        Log.i("My", "INPUT VALIDATOR: ${clipDataText}")
        val pattern = "-?\\d+ (-?\\d+ )?-?\\d+"
        return Regex(pattern).matches(clipDataText)
    }


}