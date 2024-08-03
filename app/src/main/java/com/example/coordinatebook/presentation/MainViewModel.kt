package com.example.coordinatebook.presentation

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coordinatebook.domain.models.WorldInfo
import com.example.coordinatebook.domain.usecases.worlds.AddWorldUseCase
import com.example.coordinatebook.domain.usecases.worlds.DeleteWorldUseCase
import com.example.coordinatebook.domain.usecases.worlds.GetAllWorldsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel (
    val getAllWorldsUseCase: GetAllWorldsUseCase,
    val addWorldUseCase: AddWorldUseCase,
    val deleteWorldUseCase: DeleteWorldUseCase,
): ViewModel() {

    suspend fun getWorldsList(): MutableList<WorldInfo> {
        return getAllWorldsUseCase.execute()
    }

}
