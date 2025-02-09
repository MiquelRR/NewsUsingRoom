package com.miquel.newsusingroom

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.miquel.newsusingroom.databinding.ActivityNewslistBinding
import com.miquel.newsusingroom.repository.Liked
import com.miquel.newsusingroom.repository.NewsApplication.Companion.database
import com.miquel.newsusingroom.repository.NewsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsListActivity : AppCompatActivity() {
    private lateinit var newsList: List<NewsItem>
    private lateinit var likedNewsIds : Set<Int>
    private var viewOnlyLiked: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userId= preferences.getInt("logged_user_id", -1)
        Toast.makeText(this, "User ID: $userId", Toast.LENGTH_SHORT).show()
        super.onCreate(savedInstanceState)
        val binding = ActivityNewslistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.myFab.setOnClickListener {
            preferences.edit().putInt("news_item_to_edit", -1).apply()
            startActivity(Intent(this, UpdateNewsActivity::class.java))
        }

        loadNews(userId, binding)

        val bottomNavigationView=binding.bottomNavigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_exit -> {
                    likedNewsIds = emptySet()
                    preferences.edit().remove("remembered_user_mail").apply()
                    intent = Intent(this@NewsListActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Cierra la actividad
                    true
                }
                R.id.nav_favorites -> {
                    viewOnlyLiked = true
                    loadNews(userId, binding)
                    // Filtrar solo favoritos
                    true
                }
                R.id.nav_all -> {
                    viewOnlyLiked = false
                    loadNews(userId, binding)
                    // Mostrar todas las noticias
                    true
                }
                else -> false
            }
        }

    }

    private fun loadNews(
        userId: Int,
        binding: ActivityNewslistBinding
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            likedNewsIds = loadLikesForUser(userId)

            newsList = if (viewOnlyLiked) {
                if (likedNewsIds.isNotEmpty()) database.newsArticleDao().getAllNews()
                    .filter { likedNewsIds.contains(it.id) } else emptyList()
            } else {
                database.newsArticleDao().getAllNews()
            }

            withContext(Dispatchers.Main) {
                binding.myRecyclerView.layoutManager = LinearLayoutManager(this@NewsListActivity)
                binding.myRecyclerView.adapter =
                    MyAdapter(newsList, likedNewsIds) { newsItem, isLiked ->
                        CoroutineScope(Dispatchers.IO).launch {
                            if (isLiked) {
                                database.likedDao().likeNews(Liked(userId, newsItem.id))
                            } else {
                                database.likedDao().unlikeNews(Liked(userId, newsItem.id))
                            }
                        }
                    }
            }
        }
    }

    private suspend fun loadLikesForUser(userId: Int): Set<Int> {
        val likedNews = database.likedDao().getLikedNews(userId)
        return likedNews.map { it.news_id }.toSet() // Convertir en Set<Int> para acceso rápido
    }
}