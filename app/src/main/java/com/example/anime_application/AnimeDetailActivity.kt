package com.example.anime_application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AnimeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_detail)

        // Получаем данные о выбранном аниме из Intent
        val animeId = intent.getIntExtra("anime_id", -1)

        // Загружаем данные об аниме с использованием animeId и отображаем их на экране
    }
}
