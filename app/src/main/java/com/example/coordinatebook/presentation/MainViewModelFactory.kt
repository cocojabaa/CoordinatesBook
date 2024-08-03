package com.example.coordinatebook.presentation

import android.content.Context
import android.widget.LinearLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coordinatebook.data.worlds.WorldsRepositoryImpl
import com.example.coordinatebook.domain.WorldsRepository
import com.example.coordinatebook.domain.usecases.worlds.AddWorldUseCase
import com.example.coordinatebook.domain.usecases.worlds.DeleteWorldUseCase
import com.example.coordinatebook.domain.usecases.worlds.GetAllWorldsUseCase

class MainViewModelFactory(context: Context): ViewModelProvider.Factory {

    val worldsRepository: WorldsRepository by lazy { WorldsRepositoryImpl(context) }

    val getAllWorldsUseCase: GetAllWorldsUseCase by lazy { GetAllWorldsUseCase(worldsRepository) }
    val addWorldUseCase: AddWorldUseCase by lazy { AddWorldUseCase(worldsRepository) }
    val deleteWorldUseCase: DeleteWorldUseCase by lazy { DeleteWorldUseCase(worldsRepository) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            getAllWorldsUseCase = getAllWorldsUseCase,
            addWorldUseCase = addWorldUseCase,
            deleteWorldUseCase = deleteWorldUseCase,
        ) as T
    }
}