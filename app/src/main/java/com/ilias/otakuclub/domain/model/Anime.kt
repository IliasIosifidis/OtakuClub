package com.ilias.otakuclub.domain.model

data class Anime(
    val id: Int,
    val title: String,
    val imageUrlSmall: String?,
    val score: Double?
)