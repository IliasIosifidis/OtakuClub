package com.ilias.otakuclub.data.mapper

import com.ilias.otakuclub.data.remote.dto.GenreDto
import com.ilias.otakuclub.data.remote.dto.GenresResponseDto
import com.ilias.otakuclub.domain.model.AnimeCategories

fun GenresResponseDto.toDomainAnimeCategories(): List<AnimeCategories> =
    data.map { dto: GenreDto ->
        AnimeCategories(
            category = dto.name,
            count = dto.count
        )
    }