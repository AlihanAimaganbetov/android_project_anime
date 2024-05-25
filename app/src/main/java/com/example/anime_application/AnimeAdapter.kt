package com.example.anime_application


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AnimeAdapter(
    private val onItemClick: (Anime) -> Unit,
    private val onFavoriteClick: (Anime) -> Unit
) : ListAdapter<Anime, AnimeAdapter.AnimeViewHolder>(AnimeDiffCallback()) {

    private val animeList = mutableListOf<Anime>()
    private var originalAnimeList = listOf<Anime>()
    private val favoriteAnimeList = mutableListOf<Anime>()


    fun addToFavorites(anime: Anime) {
        if (anime.isFavorite) {

            anime.isFavorite = true
            favoriteAnimeList.add(anime)
            notifyItemChanged(animeList.indexOf(anime))
        }
    }

    fun removeFromFavorites(anime: Anime) {
        if (!anime.isFavorite) {
            anime.isFavorite = false

            favoriteAnimeList.remove(anime)
            notifyItemChanged(animeList.indexOf(anime))
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addItems(items: List<Anime>) {
        animeList.addAll(items)
        originalAnimeList = animeList.toList() // Обновляем оригинальный список
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        val filteredList = if (query.isBlank()) {
            originalAnimeList.toList() // Вернуть оригинальный список, если запрос пустой
        } else {
            animeList.filter { anime ->
                anime.title.contains(query, ignoreCase = true)
            }
        }
        updateList(filteredList)
    }

    // Метод для обновления списка
    @SuppressLint("NotifyDataSetChanged")
    private fun updateList(newList: List<Anime>) {
        animeList.clear() // Очищаем текущий список
        animeList.addAll(newList) // Добавляем отфильтрованные элементы
        notifyDataSetChanged() // Уведомляем адаптер о изменении данных
    }

    fun sortByScore() {
        animeList.sortWith(Comparator { anime1, anime2 -> anime2.score.compareTo(anime1.score) })
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_anime, parent, false)
        return AnimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = animeList[position]
        holder.bind(anime)
        holder.itemView.setOnClickListener {
            onItemClick(anime)
        }
        holder.favoriteButton.setOnClickListener {
            anime.isFavorite = !anime.isFavorite
            onFavoriteClick(anime)
            holder.favoriteButton.text = if (!anime.isFavorite) "Remove from Favorites" else "Add to Favorites"
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return animeList.size
    }

    inner class AnimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val synopsisTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val genreTextView: TextView = itemView.findViewById(R.id.genreTextView)
        private val airedTextView: TextView = itemView.findViewById(R.id.airedTextView)
        private val episodesTextView: TextView = itemView.findViewById(R.id.episodesTextView)
        private val membersTextView: TextView = itemView.findViewById(R.id.membersTextView)
        private val popularityTextView: TextView = itemView.findViewById(R.id.popularityTextView)
        private val rankedTextView: TextView = itemView.findViewById(R.id.rankedTextView)
        private val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val favoriteButton: Button = itemView.findViewById(R.id.favoriteButton) // Add this line
        fun bindFromFavoriteList(anime: Anime) {
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
        }

        fun bind(anime: Anime) {
            titleTextView.text = anime.title
            synopsisTextView.text = anime.synopsis
            genreTextView.text = anime.genre
            airedTextView.text = anime.aired
            episodesTextView.text = anime.episodes.toString()
            membersTextView.text = anime.members
            popularityTextView.text = anime.popularity
            rankedTextView.text = anime.ranked.toString()
            scoreTextView.text = anime.score.toString()


            if (anime.img_url.isNotEmpty()) {
                // Загружаем и отображаем изображение с помощью Glide
                Glide.with(itemView.context)
                    .load(anime.img_url)
                    .centerCrop()
                    .placeholder(R.drawable.image) // Заглушка, пока изображение загружается
                    .into(imageView)
                imageView.visibility = View.VISIBLE // Показываем ImageView
            } else {
                // Если ссылки на изображение нет, скрываем ImageView
                imageView.visibility = View.GONE
            }
        }
    }
}

class AnimeDiffCallback : DiffUtil.ItemCallback<Anime>() {
    override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
        return oldItem == newItem
    }
}
