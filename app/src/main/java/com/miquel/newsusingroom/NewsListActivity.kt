package com.miquel.newsusingroom

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.miquel.newsusingroom.databinding.ActivityNewslistBinding
import com.miquel.newsusingroom.entities.NewsItem
import com.miquel.newsusingroom.repository.NewsApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsListActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        var cardList: MutableList<NewsItem> = mutableListOf()
        cardList.clear()
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        //val category = preferences.getString(R.string.chosen_category.toString(), "ciencia")
        val binding = ActivityNewslistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val rssUrl = categories[category]?:R.string.default_rss_url.toString()
        binding.myFab.setOnClickListener {
            preferences.edit().putInt("news_item_to_edit", -1).apply()
            startActivity(Intent(this, UpdateNewsActivity::class.java))
        }

        CoroutineScope(Dispatchers.Main).launch {
            //cardList = getNewsList(rssUrl)
            cardList = NewsApplication.database.newsArticleDao().getAllNews()
            binding.myRecyclerView.layoutManager = LinearLayoutManager(this@NewsListActivity)
            binding.myRecyclerView.adapter = MyAdapter(cardList)
        }

    }
}