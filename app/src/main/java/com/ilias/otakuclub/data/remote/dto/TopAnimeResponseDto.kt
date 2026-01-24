package com.ilias.otakuclub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TopAnimeResponseDto(
    val data: List<AnimeDto>
)
data class AnimeDto(
    @SerializedName("mal_id")
    val id: Int,
    val title: String,
    val images: AnimeImagesDto,
    val score: Double?
)

