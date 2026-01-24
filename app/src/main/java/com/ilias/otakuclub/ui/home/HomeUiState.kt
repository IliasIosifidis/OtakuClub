package com.ilias.otakuclub.ui.home

import androidx.compose.runtime.Composable
import com.ilias.otakuclub.domain.model.Anime

@Composable
fun HomeUiState(){
    val isLoading: Boolean = false
    val anime: List<Anime> = emptyList()
    val errorMessage: String? = null
}