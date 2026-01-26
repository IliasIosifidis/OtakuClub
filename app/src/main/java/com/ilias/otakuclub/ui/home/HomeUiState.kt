package com.ilias.otakuclub.ui.home

import com.ilias.otakuclub.domain.model.Anime

data class HomeUiState(
    val anime: List<Anime> = emptyList(),
    val errorMessage: String? = null,
    val page: Int = 1,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val endReached: Boolean = false
)