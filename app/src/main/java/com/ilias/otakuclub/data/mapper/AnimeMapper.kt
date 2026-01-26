package com.ilias.otakuclub.data.mapper

import com.ilias.otakuclub.data.remote.dto.AnimeDto
import com.ilias.otakuclub.domain.model.Anime

fun AnimeDto.toDomainAnime(): Anime {
    val jpg = images.jpg

    val small = jpg.imageUrlL ?: jpg.imageUrlS

    return Anime(
        id = id,
        title = title,
        imageUrlSmall = small,
        score = score
    )
}