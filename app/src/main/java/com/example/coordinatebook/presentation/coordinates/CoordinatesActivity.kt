package com.example.coordinatebook.presentation.coordinates

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
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
            Log.i("My", "WORLD ID IN COORDINATES ACTIVITY: ${worldId}")
            val coordinatesList = async{getCoordinatesByIdUseCase.execute(worldId)}.await()
            adapter = CoordinatesRecyclerAdapter(coordinatesList, this@CoordinatesActivity)
            binding.coordinatesRecycler.adapter = adapter
            binding.coordinatesRecycler.layoutManager = LinearLayoutManager(this@CoordinatesActivity)
        }
    }

    override fun onCoordinatesLongClick(coordinatesInfo: CoordinatesInfo) {
        showDeleteCoordinatesDialog(coordinatesInfo)
    }

    fun showAddCoordinatesDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.add_coordinates_dialog, null)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val addButton = dialogBinding.findViewById<Button>(R.id.addCoordinatesButton)
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
            Log.i("My", "IN DIALOG: WORLDID=${coordinatesInfo.worldId} Y=${coordinatesInfo.y} DIMEN=${coordinatesInfo.dimension}")
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


}