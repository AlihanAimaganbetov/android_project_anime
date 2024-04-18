package com.example.anime_application
import java.io.Serializable

data class Anime(
    val id: Int,
    val title: String,
    val synopsis: String,
    val genre: String,
    val aired: String,
    val episodes: Int,
    val members: String,
    val popularity: String,
    val ranked: Int,
    val score: Double,
    val img_url: String,
    val link: String
) : Serializable

