package com.ilias.otakuclub.domain.repository

import com.ilias.otakuclub.data.remote.dto.AnimeDetailsDto
import com.ilias.otakuclub.domain.model.Anime
import com.ilias.otakuclub.domain.model.AnimeDetails

interface AnimeRepository{

    suspend fun getTopAnime(page: Int = 1): List<Anime>

    suspend fun searchAnime(q: String, page: Int = 1): List<Anime>

    suspend fun getAnimeDetails(id: Int): AnimeDetails

}