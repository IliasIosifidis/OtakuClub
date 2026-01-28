package com.ilias.otakuclub.ui.category

import com.ilias.otakuclub.domain.repository.AnimeRepository

class CategoryViewModelFactory(
    private val repo: AnimeRepository
): androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T: androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}