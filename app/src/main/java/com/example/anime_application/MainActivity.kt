package com.example.anime_application
import com.bumptech.glide.Glide
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anime_application.AnimeAdapter
import com.example.anime_application.AnimeApiService
import com.example.anime_application.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.os.Parcel
import android.os.Parcelable


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: AnimeAdapter
    private var isLoading = false
    private var currentPage = 1
    private val PAGE_SIZE = 10
    private var totalItems = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        adapter = AnimeAdapter { anime ->
            // Создаем Intent для запуска новой Activity
            val intent = Intent(this@MainActivity, AnimeDetailActivity::class.java)

            // Передаем объект anime в новую Activity
            intent.putExtra("anime", anime) // Предполагается, что объект Anime реализует Serializable или Parcelable

            // Запускаем новую Activity
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Добавляем слушатель прокрутки для RecyclerView
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Проверяем, достигли ли мы конца списка и не загружаем ли уже данные
                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadNextPage()
                }
            }
        })

        // Загружаем первую страницу данных
        loadNextPage()
    }

    private fun loadNextPage() {
        isLoading = true

        // Создаем экземпляр Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000") // URL вашего API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(AnimeApiService::class.java)

        // Выполняем запрос на получение данных для текущей страницы
        GlobalScope.launch(Dispatchers.IO) {
            val response = service.getAnimeList().execute()

            if (response.isSuccessful) {
                val animeList = response.body()
                animeList?.let { list ->
                    // Обновляем UI на главном потоке
                    launch(Dispatchers.Main) {
                        adapter.addItems(list)
                        currentPage++
                        isLoading = false
                    }
                }
            }
        }
    }
}
