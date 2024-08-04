package com.example.coordinatebook.presentation.coordinates

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coordinatebook.R
import com.example.coordinatebook.data.coordinates.CoordinatesRepositoryImpl
import com.example.coordinatebook.databinding.ActivityWorldBinding
import com.example.coordinatebook.domain.CoordinatesRepository
import com.example.coordinatebook.domain.models.CoordinatesInfo
import com.example.coordinatebook.domain.usecases.coordinates.AddCoordinatesUseCase
import com.example.coordinatebook.domain.usecases.coordinates.DeleteCoordinatesUseCase
import com.example.coordinatebook.domain.usecases.coordinates.GetCoordinatesByIdUseCase
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
//    val deleteCoordinatesUseCase: DeleteCoordinatesUseCase by lazy { DeleteCoordinatesUseCase(coordinatesRepository) }

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
        // TODO
    }

    fun showAddCoordinatesDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.add_coordinates_dialog, null)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

    }


}