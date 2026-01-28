package com.ilias.otakuclub.domain.repository

import com.ilias.otakuclub.domain.model.Anime
import com.ilias.otakuclub.domain.model.AnimeCategories
import com.ilias.otakuclub.domain.model.AnimeDetails

interface AnimeRepository{

    suspend fun getTopAnime(page: Int = 1): List<Anime>

    suspend fun searchAnime(q: String, page: Int = 1, genres: String?): List<Anime>

    suspend fun getAnimeDetails(id: Int): AnimeDetails

    suspend fun getCategories(): List<AnimeCategories>

}