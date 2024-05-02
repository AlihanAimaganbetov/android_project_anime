package com.example.anime_application
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
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

    @SuppressLint("SetTextI18n")
    private fun displayAnimeDetails(anime: Anime) {
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val synopsisTextView: TextView = findViewById(R.id.descriptionTextView)
        val genreTextView: TextView = findViewById(R.id.genreTextView)
        val airedTextView: TextView = findViewById(R.id.airedTextView)
        val episodesTextView: TextView = findViewById(R.id.episodesTextView)

        val popularityTextView: TextView = findViewById(R.id.popularityTextView)
        val rankedTextView: TextView = findViewById(R.id.rankedTextView)
        val scoreTextView: TextView = findViewById(R.id.scoreTextView)
        val imageView: ImageView = findViewById(R.id.imageView)
        val linkTextView: TextView = findViewById(R.id.linkTextView)

        titleTextView.text = "Title " + anime.title
        synopsisTextView.text ="Description " + anime.synopsis
        genreTextView.text = "Genre "+ anime.genre
        airedTextView.text = "Aired " + anime.aired
        episodesTextView.text ="Episodes " + anime.episodes.toString()
        popularityTextView.text = "popularity " +anime.popularity
        rankedTextView.text ="ranked "+ anime.ranked.toString()
        scoreTextView.text ="score "+ anime.score.toString()
        linkTextView.text ="link "+ anime.link
        if (anime.img_url.isNotEmpty()) {
            Glide.with(this)
                .load(anime.img_url)
                .centerCrop()
                .placeholder(R.drawable.image)
                .into(imageView)
        } else {
            imageView.visibility = View.GONE
        }
    }
}

