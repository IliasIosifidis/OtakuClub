package com.ilias.otakuclub.data.repository

import com.ilias.otakuclub.data.mapper.toDomainAnime
import com.ilias.otakuclub.data.mapper.toDomainAnimeCategories
import com.ilias.otakuclub.data.mapper.toDomainAnimeDetail
import com.ilias.otakuclub.data.remote.ApiClient.jikanApi
import com.ilias.otakuclub.data.remote.JikanApiService
import com.ilias.otakuclub.domain.model.Anime
import com.ilias.otakuclub.domain.model.AnimeCategories
import com.ilias.otakuclub.domain.model.AnimeDetails
import com.ilias.otakuclub.domain.repository.AnimeRepository


class AnimeRepositoryImpl(jikanApi1: JikanApiService) : AnimeRepository {
    private val api = jikanApi

    override suspend fun getTopAnime(page: Int): List<Anime> {
        val res = api.getTopAnime(page)
        return res.data.map { it.toDomainAnime() }
    }

    override suspend fun getAnimeDetails(id: Int): AnimeDetails {
        val res = api.getAnimeDetails(id)
        return res.data.toDomainAnimeDetail(id)
    }

    override suspend fun getCategories(): List<AnimeCategories> {
        val res = api.getGenre()
        return res.toDomainAnimeCategories()
    }

    override suspend fun searchAnime(
        q: String,
        page: Int,
        genres: String?,
        year: Int?,
        sfw: Boolean,
        startDate: String?,
        endDate: String?
    ): List<Anime> {
        val computedStart = startDate ?: year?.let { "$it-01-01" }
        val computedEnd = endDate ?: year?.let { "$it-31-12" }

        val res = api.searchAnime(
            query = q.trim().takeIf { it.isNotBlank() },
            page = page,
            genres = genres?.takeIf { it.isNotBlank() },
            sfw = sfw,
            startDate = computedStart,
            endDate = computedEnd
        )

        return res.data.map { it.toDomainAnime() }
    }
}