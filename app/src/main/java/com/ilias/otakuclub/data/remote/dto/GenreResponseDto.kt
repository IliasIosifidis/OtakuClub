package com.ilias.otakuclub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GenresResponseDto(
    val data: List<GenreDto>
)

data class GenreDto(
    @SerializedName("mal_id")
    val id: Int,
    val name: String,
    val count: Int,
    val url: String
)