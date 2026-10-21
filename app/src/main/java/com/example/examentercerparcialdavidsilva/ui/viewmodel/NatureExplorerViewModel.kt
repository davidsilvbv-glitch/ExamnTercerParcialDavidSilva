package com.example.examentercerparcialdavidsilva.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.examentercerparcialdavidsilva.data.local.FaunaFactDataEntity
import com.example.examentercerparcialdavidsilva.data.local.NatureWeatherDataEntity
import com.example.examentercerparcialdavidsilva.data.repository.NatureExplorerCoordinatorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class NatureUiState(
    val isLoading: Boolean = false,
    val weatherList: List<NatureWeatherDataEntity> = emptyList(),
    val faunaFactsList: List<FaunaFactDataEntity> = emptyList(),
    val errorMessage: String? = null
)

class NatureExplorerViewModel(
    private val coordinatorRepository: NatureExplorerCoordinatorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NatureUiState(isLoading = true))
    val uiState: StateFlow<NatureUiState> = _uiState.asStateFlow()

    init {
        observeLocalData()
        refreshDataFromNetwork()
    }

    private fun observeLocalData() {
        viewModelScope.launch {
            try {
                combine(
                    coordinatorRepository.getNatureWeatherLocal(),
                    coordinatorRepository.getFaunaFactsLocal()
                ) { weatherList, faunaFactsList ->
                    _uiState.value.copy(
                        weatherList = weatherList,
                        faunaFactsList = faunaFactsList
                    )
                }.collect { updatedState ->
                    _uiState.value = updatedState
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al leer datos locales: ${e.localizedMessage}"
                )
            }
        }
    }

    fun refreshDataFromNetwork() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = coordinatorRepository.fetchAndStoreAllData()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = result.exceptionOrNull()?.localizedMessage
            )
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            try {
                coordinatorRepository.clearAllData()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al borrar la base de datos: ${e.localizedMessage}"
                )
            }
        }
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

class NatureExplorerViewModelFactory(
    private val repository: NatureExplorerCoordinatorRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NatureExplorerViewModel::class.java)) {
            return NatureExplorerViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel Class desconocido")
    }
}
