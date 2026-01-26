package com.ilias.otakuclub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilias.otakuclub.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repo: AnimeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTopAnime()
    }

    fun loadTopAnime(page: Int = 1){
        viewModelScope.launch {
            runCatching {
                repo.getTopAnime(page)
            }.onSuccess { list ->
                _uiState.update { it.copy(isLoading = false, anime = list) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false,errorMessage = e.message ?: "Something went wrong")
                }
            }
        }
    }

    fun loadNextPage(){
        val state = _uiState.value
        if (state.isLoading || state.isLoadingMore || state.endReached) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true, errorMessage = null) }

            runCatching {
                repo.getTopAnime(page = state.page + 1)
            }.onSuccess { newItems ->
                _uiState.update {
                    val reachedEnd = newItems.isEmpty()
                    it.copy(
                        isLoadingMore = false,
                        page = if (reachedEnd) it.page else it.page + 1,
                        endReached = reachedEnd,
                        anime = it.anime + newItems
                    )
                }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        isLoadingMore = false,
                        errorMessage = e.message ?: "Something went wrong")
                }
            }
        }
    }
}