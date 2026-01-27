package com.ilias.otakuclub.ui.details

import com.ilias.otakuclub.domain.repository.AnimeRepository

class DetailsViewModelFactory(
    private val repo: AnimeRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T: androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DetailsViewModel(repo) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class: $modelClass")
    }
}