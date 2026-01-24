package com.ilias.otakuclub.data.mapper

import androidx.compose.runtime.Composable
import com.ilias.otakuclub.data.remote.dto.AnimeDetailsDto
import com.ilias.otakuclub.domain.model.AnimeDetails

@Composable
fun AnimeDetailsDto.toDomainAnimeDetail(id: Int): AnimeDetails {

    val jpg = images?.jpg

    return AnimeDetails(
        id = id,
        imageUrlLarge = jpg?.imageUrlL ?: jpg?.imageUrl,
        trailer = trailer?.embedUrl ?: trailer?.youtube ?: trailer?.url,
        titleJap = titleJap,
        episodes = episodes,
        status = status,
        airedFrom = aired?.from,
        airedTo = aired?.to,
        duration = duration,
        synopsis = synopsis,
        background = background,
        year = year,
        genres = genres?.map { it.name } ?: emptyList()
    )
}