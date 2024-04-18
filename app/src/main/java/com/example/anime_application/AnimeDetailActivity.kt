package com.example.anime_application
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class AnimeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_detail)

        // Получаем данные о выбранном аниме из Intent
        val anime = intent.getSerializableExtra("anime") as? Anime

        // Проверяем, что аниме не null и отображаем информацию на экране
        anime?.let { displayAnimeDetails(it) }
    }

    private fun displayAnimeDetails(anime: Anime) {
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val synopsisTextView: TextView = findViewById(R.id.descriptionTextView)
        val genreTextView: TextView = findViewById(R.id.genreTextView)
        val airedTextView: TextView = findViewById(R.id.airedTextView)
        val episodesTextView: TextView = findViewById(R.id.episodesTextView)
        val membersTextView: TextView = findViewById(R.id.membersTextView)
        val popularityTextView: TextView = findViewById(R.id.popularityTextView)
        val rankedTextView: TextView = findViewById(R.id.rankedTextView)
        val scoreTextView: TextView = findViewById(R.id.scoreTextView)
//  val imageView: ImageView = findViewById(R.id.imageView)
        val linkTextView: TextView = findViewById(R.id.linkTextView)

        titleTextView.text = anime.title
        synopsisTextView.text = anime.synopsis
        genreTextView.text = anime.genre
        airedTextView.text = anime.aired
        episodesTextView.text = anime.episodes.toString()
        membersTextView.text = anime.members
        popularityTextView.text = anime.popularity
        rankedTextView.text = anime.ranked.toString()
        scoreTextView.text = anime.score.toString()
        linkTextView.text = anime.link
    }

}
