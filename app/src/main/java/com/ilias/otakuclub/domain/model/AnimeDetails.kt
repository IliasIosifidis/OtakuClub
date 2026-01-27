package com.ilias.otakuclub.domain.model

data class AnimeDetails(
    val id: Int,
    val title: String,
    val imageUrlLarge: String?,
    val trailer: String?,
    val titleJap: String?,
    val episodes: Int?,
    val status: String?,
    val airedFrom: String?,
    val airedTo: String?,
    val duration: String?,
    val synopsis: String?,
    val background: String?,
    val year: Int?,
    val genres: List<String>,
    val score: Double?
)