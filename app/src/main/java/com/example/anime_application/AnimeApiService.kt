package com.example.anime_application
import retrofit2.Call
import retrofit2.http.GET

interface AnimeApiService {
    @GET("/api/anime")
    fun getAnimeList(): Call<List<Anime>>
}
