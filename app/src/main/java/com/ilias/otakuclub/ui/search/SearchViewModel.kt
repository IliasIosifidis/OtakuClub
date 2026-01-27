package com.ilias.otakuclub.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilias.otakuclub.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repo: AnimeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState : StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun loadSearchAnime(q: String){

        viewModelScope.launch {
            val query = q.trim()
            if (query.isEmpty()){
                _uiState.update { it.copy(isLoading = false, searchResults = emptyList(), errorMessage = null) }
                return@launch
            }
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                repo.searchAnime(query)
            }.onSuccess { list ->
                _uiState.update { it.copy(isLoading = false, searchResults = list) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Something went wrong") }
            }
        }
    }
}