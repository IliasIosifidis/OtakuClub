package com.ilias.otakuclub.ui.details

import com.ilias.otakuclub.domain.model.AnimeDetails

data class DetailsUiState(
    val isLoading: Boolean = false,
    val animeDetails: AnimeDetails? = null,
    val errorMessage: String? = null
)