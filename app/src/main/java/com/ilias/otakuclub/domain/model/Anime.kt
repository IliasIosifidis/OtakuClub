package com.ilias.otakuclub.domain.model

import androidx.compose.runtime.Composable


data class Anime(
    val id: Int,
    val title: String,
    val imageUrlSmall: String?,
    val score: Double?
)