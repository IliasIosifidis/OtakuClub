package com.ilias.otakuclub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SearchAnimeResponseDto(
    val data: List<AnimeDto>,
    val pagination: PaginationDto
)

data class PaginationDto(
    @SerializedName("last_visible_page")
    val lastVisiblePage: Int,

    @SerializedName("has_next_page")
    val hasNextPage: Boolean,

    @SerializedName("current_page")
    val currentPage: Int
)
