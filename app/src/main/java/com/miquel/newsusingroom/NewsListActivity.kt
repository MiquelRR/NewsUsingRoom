package com.miquel.newsusingroom

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.miquel.newsusingroom.databinding.ActivityNewslistBinding
import com.miquel.newsusingroom.repository.Liked
import com.miquel.newsusingroom.repository.NewsItem
import com.miquel.newsusingroom.repository.User
import com.miquel.newsusingroom.repository.NewsApplication
import com.miquel.newsusingroom.repository.NewsApplication.Companion.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsListActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userId= preferences.getInt("user_id", -1)
        var user: User? = null
        lifecycleScope.launch {
            user = database.userDao().getUserById(userId)
        }
        var cardList: MutableList<NewsItem> = mutableListOf()
        cardList.clear()
        super.onCreate(savedInstanceState)
        //val category = preferences.getString(R.string.chosen_category.toString(), "ciencia")
        val binding = ActivityNewslistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val rssUrl = categories[category]?:R.string.default_rss_url.toString()
        binding.myFab.setOnClickListener {
            preferences.edit().putInt("news_item_to_edit", -1).apply()
            startActivity(Intent(this, UpdateNewsActivity::class.java))
        }

        CoroutineScope(Dispatchers.IO).launch {
            val likedNewsIds = loadLikesForUser(userId)
            val allNews = database.newsArticleDao().getAllNews()

            withContext(Dispatchers.Main) {
                binding.myRecyclerView.layoutManager = LinearLayoutManager(this@NewsListActivity)
                binding.myRecyclerView.adapter = MyAdapter(allNews, likedNewsIds) { newsItem, isLiked ->
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

        /*
        CoroutineScope(Dispatchers.Main).launch {
            //cardList = getNewsList(rssUrl)
            cardList = database.newsArticleDao().getAllNews()
            binding.myRecyclerView.layoutManager = LinearLayoutManager(this@NewsListActivity)
            binding.myRecyclerView.adapter = MyAdapter(cardList, loadLikesForUser(userId))
        }
        */

    }
    private suspend fun loadLikesForUser(userId: Int): Set<Int> {
        val likedNews = database.likedDao().getLikedNews(userId)
        return likedNews.map { it.news_id }.toSet() // Convertir en Set<Int> para acceso rápido
    }
}