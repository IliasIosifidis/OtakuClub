package com.ilias.otakuclub.ui.category

import com.ilias.otakuclub.domain.model.AnimeCategories

data class CategoryUiState(
    val isLoading: Boolean = false,
    val categories: List<AnimeCategories> = emptyList(),
    val error: String? = null
)
