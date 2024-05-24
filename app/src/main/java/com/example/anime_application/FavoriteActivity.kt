package com.example.anime_application

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: AnimeAdapter
    private lateinit var favoriteAnimeList: List<Anime>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        // Получаем список избранных аниме из Intent
        favoriteAnimeList = intent.getSerializableExtra("favoriteAnimeList") as List<Anime>
        Log.d("FavoriteActivity", "Favorite Anime List: $favoriteAnimeList")

        // Настраиваем RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.favoriteRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AnimeAdapter(onItemClick = { anime ->
            val intent = Intent(this@FavoriteActivity, AnimeDetailActivity::class.java)
            intent.putExtra("anime", anime)
            startActivity(intent)
        }, onFavoriteClick = { anime ->
            if (!anime.isFavorite) {
                adapter.addToFavorites(anime)
            } else {
                adapter.removeFromFavorites(anime)
            }
            Log.d("favoriteUpdated", "Favorite Anime List Updated: $favoriteAnimeList")
        })

        recyclerView.adapter = adapter

        // Передаем данные в адаптер
        adapter.submitList(favoriteAnimeList)
    }
}
