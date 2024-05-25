package com.example.anime_application

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: AnimeAdapter
    private lateinit var favoriteAnimeList: ArrayList<Anime> // Change to ArrayList

    @SuppressLint("MissingInflatedId", "SetTextI18n", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        // Retrieve the ArrayList from the Intent
        favoriteAnimeList =
            intent.getSerializableExtra("favoriteAnimeList") as? ArrayList<Anime> ?: arrayListOf()
        Log.d("FavoriteActivity", "Favorite Anime List: $favoriteAnimeList")

        val recyclerView: RecyclerView = findViewById(R.id.favoriteRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AnimeAdapter(onItemClick = { anime ->
            val intent = Intent(this@FavoriteActivity, AnimeDetailActivity::class.java)
            intent.putExtra("anime", anime)
            startActivity(intent)
        }, onFavoriteClick = { anime ->

            Log.d("favoriteUpdated", "Favorite Anime List Updated: $favoriteAnimeList")
        })

        recyclerView.adapter = adapter
        val parentLayout: ConstraintLayout = findViewById(R.id.parentLayout)

        // Submit the favoriteAnimeList to the adapter
        adapter.submitList(favoriteAnimeList)

        for (anime in favoriteAnimeList) {
            // Создаем новый View для каждого элемента аниме
            val itemView = LayoutInflater.from(this@FavoriteActivity).inflate(R.layout.favorite_item_anime, findViewById(R.id.parentLayout), false)

            // Находим TextView в itemView и устанавливаем соответствующие значения
            val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
            val synopsisTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
            val genreTextView: TextView = itemView.findViewById(R.id.genreTextView)
            val airedTextView: TextView = itemView.findViewById(R.id.airedTextView)
            val episodesTextView: TextView = itemView.findViewById(R.id.episodesTextView)
            val membersTextView: TextView = itemView.findViewById(R.id.membersTextView)
            val popularityTextView: TextView = itemView.findViewById(R.id.popularityTextView)
            val rankedTextView: TextView = itemView.findViewById(R.id.rankedTextView)
            val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)
            val imageView: ImageView = itemView.findViewById(R.id.imageView)

            titleTextView.text = "Title: ${anime.title}"
            synopsisTextView.text = "Synopsis: ${anime.synopsis}"
            genreTextView.text = "Genre: ${anime.genre}"
            airedTextView.text = "Aired: ${anime.aired}"
            episodesTextView.text = "Episodes: ${anime.episodes}"
            membersTextView.text = "Members: ${anime.members}"
            popularityTextView.text = "Popularity: ${anime.popularity}"
            rankedTextView.text = "Ranked: ${anime.ranked}"
            scoreTextView.text = "Score: ${anime.score}"

            if (anime.img_url.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(anime.img_url)
                    .centerCrop()
                    .placeholder(R.drawable.image)
                    .into(imageView)
                imageView.visibility = View.VISIBLE
            } else {
                imageView.visibility = View.GONE
            }

            // Добавляем itemView в родительский Layout
            parentLayout.addView(itemView)
        }


    }
}

