package com.ilias.otakuclub.data.repository

import com.ilias.otakuclub.data.mapper.toDomainAnime
import com.ilias.otakuclub.data.mapper.toDomainAnimeDetail
import com.ilias.otakuclub.data.remote.ApiClient.jikanApi
import com.ilias.otakuclub.domain.model.Anime
import com.ilias.otakuclub.domain.model.AnimeDetails
import com.ilias.otakuclub.domain.repository.AnimeRepository


class AnimeRepositoryImpl : AnimeRepository {
    private val api = jikanApi

    override suspend fun getTopAnime(page: Int): List<Anime> {
        val res = api.getTopAnime(page)
        return res.data.map { it.toDomainAnime() }
    }

    override suspend fun getAnimeDetails(id: Int): AnimeDetails {
        val res = api.getAnimeDetails(id)
        return res.data.toDomainAnimeDetail(id)
    }

    override suspend fun searchAnime(q: String, page: Int): List<Anime> {
        val res = api.searchAnime(q)
        return res.data.map { it.toDomainAnime() }
    }
}