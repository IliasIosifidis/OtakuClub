package com.ilias.otakuclub.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilias.otakuclub.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel(private val repo: AnimeRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching {
                repo.getCategories()
            }.onSuccess { list ->
                _uiState.update { it.copy(isLoading = false, categories = list) }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load categories"
                    )
                }
            }
        }
    }
}