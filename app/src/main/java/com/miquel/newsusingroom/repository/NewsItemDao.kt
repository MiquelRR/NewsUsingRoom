package com.miquel.newsusingroom.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.miquel.newsusingroom.entities.NewsItem

@Dao
interface NewsItemDao {
    @Query("SELECT * FROM news_article")
    suspend fun getAllNews(): MutableList<NewsItem>
    @Insert
    suspend fun addNewsItem(newsItem: NewsItem)
    @Update
    suspend fun updateNewsItem(newsItem: NewsItem)
    @Delete
    suspend fun deleteNewsItem(newsItem: NewsItem)



}