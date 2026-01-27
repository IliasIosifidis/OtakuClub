package com.ilias.otakuclub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeDetailsResponseDto(
    val data: AnimeDetailsDto
)

data class AnimeDetailsDto(
    val title: String,
    val images: AnimeImagesDto?,
    val trailer: TrailerDto? = null,
    @SerializedName("title_japanese")
    val titleJap: String? = null,
    val episodes: Int? = null,
    val status: String? = null,
    val aired: AiredDto? = null,
    val duration: String? = null,
    val synopsis: String? = null,
    val background: String? = null,
    val year: Int? = null,
    val genres: List<GenreDto>? = null,
    val score: Double?
)

data class TrailerDto(
    @SerializedName("youtube_id")
    val youtube: String? = null,
    val url: String? = null,
    @SerializedName("embed_url")
    val embedUrl: String? = null
)

data class AiredDto(
    val from: String? = null,
    val to: String? = null
)
