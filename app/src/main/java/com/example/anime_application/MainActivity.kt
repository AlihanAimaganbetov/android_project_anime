package com.example.anime_application


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anime_application.Anime
import com.example.anime_application.AnimeAdapter
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: AnimeAdapter
    private var isLoading = false
    private var currentPage = 1
    private val animeList = ArrayList<Anime>()
    private val favoriteAnimeList = mutableListOf<Anime>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        adapter = AnimeAdapter(onItemClick = { anime ->
            val intent = Intent(this@MainActivity, AnimeDetailActivity::class.java)
            intent.putExtra("anime", anime)
            startActivity(intent)
            Log.d("favorite", favoriteAnimeList.toString())
            Log.d("isfavorite", anime.isFavorite.toString())
        }, onFavoriteClick = { anime ->
            if (anime.isFavorite) {
                favoriteAnimeList.add(anime)
                Log.d("favorite5", favoriteAnimeList.toString())
                adapter.addToFavorites(anime)
            } else {
                Log.d("favorite6", favoriteAnimeList.toString())
                favoriteAnimeList.remove(anime)
                adapter.removeFromFavorites(anime)
            }
        })


        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        loadDataFromLocalJson();
        // Добавляем слушатель прокрутки для RecyclerView
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                // Находим кнопку по ее идентификатору

                // Проверяем, достигли ли мы конца списка и не загружаем ли уже данные
                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadNextPage()
                }
            }
        })
        val sortButton: Button = findViewById(R.id.action_sort)

        // Устанавливаем обработчик нажатия на кнопку
        sortButton.setOnClickListener {
            // Вызываем метод сортировки в адаптере
            adapter.sortByScore()
        }

        val favoriteButton: Button = findViewById(R.id.FavoriteButton)
        favoriteButton.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            intent.putExtra("favoriteAnimeList", favoriteAnimeList.toTypedArray())
            startActivity(intent)
            Log.d("favoriteафсывфа", favoriteAnimeList.toString())
        }
        // Загружаем первую страницу данных
        loadNextPage()

    }

    private fun loadDataFromLocalJson() {
        animeList.clear() // Clear any existing data before loading
        try {
            // Open the JSON file from assets folder (modify "anime.json" if needed)
            val inputStreamReader = InputStreamReader(assets.open("anime.json"))
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            bufferedReader.close()
            val jsonString = stringBuilder.toString()

            // Parse JSON string using Gson (assuming your JSON structure matches Anime class)
            val gson = Gson()
            val animeArray = gson.fromJson(
                jsonString,
                Array<Anime>::class.java
            )

            // Sort anime list by score
            animeList.addAll(animeArray)


            // Update adapter with sorted data
            adapter.notifyDataSetChanged()
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle potential errors (e.g., file not found, parsing issue)
            // You may want to display an error message to the user
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort -> {
                adapter.sortByScore()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { adapter.filter(it) }
                return true
            }
        })
        return true
    }

    private fun loadNextPage() {
        if (isLoading) return // Prevent multiple simultaneous loads

        isLoading = true // Set loading flag

        // Read data from local JSON file instead of network request
        try {
            val inputStream = resources.openRawResource(R.raw.anime_data)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }

            bufferedReader.close()

            val jsonString = stringBuilder.toString()

            // Parse JSON string using Gson (assuming your JSON structure matches Anime class)
            val gson = Gson()
            val animeArray = gson.fromJson(jsonString, Array<Anime>::class.java)

            // Add new items to the adapter's list
            adapter.addItems(animeArray.toList())

            currentPage++ // Increment page number

            // Sort the adapter list by score


            // Update adapter and reset loading flag on main thread
            runOnUiThread {
                adapter.notifyDataSetChanged()
                isLoading = false
            }

        } catch (e: IOException) {
            e.printStackTrace()
            // Handle potential errors (e.g., file not found, parsing issue)
            // You may want to display an error message to the user
            runOnUiThread {
                isLoading = false // Reset loading flag even on error
            }
        }
    }


}
