package com.ilias.otakuclub.data.mapper

import com.ilias.otakuclub.data.remote.dto.AnimeDetailsDto
import com.ilias.otakuclub.domain.model.AnimeDetails

fun AnimeDetailsDto.toDomainAnimeDetail(id: Int): AnimeDetails {

    val jpg = images?.jpg

    return AnimeDetails(
        id = id,
        title = title,
        imageUrlLarge = jpg?.imageUrlL ?: jpg?.imageUrl,
        trailer = trailer?.embedUrl ?: trailer?.youtube ?: trailer?.url,
        titleJap = titleJap,
        episodes = episodes,
        status = status,
        airedFrom = aired?.from?.dropLast(15) ?: "No info",
        airedTo = aired?.to?.dropLast(15) ?: "No info",
        duration = duration ?: "No information about it's duration",
        synopsis = synopsis ?: "No information about the Anime's Synopsis",
        background = background ?: "No information about the Anime's Background",
        year = year,
        genres = genres?.map { it.name } ?: emptyList(),
        score = score
    )
}