package com.ilias.otakuclub.ui.search

import com.ilias.otakuclub.domain.model.Anime

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchResults: List<Anime> = emptyList(),
    val errorMessage: String? = null
)