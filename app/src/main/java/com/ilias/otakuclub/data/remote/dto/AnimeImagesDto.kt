package com.ilias.otakuclub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeImagesDto(
    val jpg: AnimeImageJpgDto
)

data class AnimeImageJpgDto(
    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("small_image_url")
    val imageUrlS: String? = null,
    @SerializedName("large_image_url")
    val imageUrlL: String? = null
)
