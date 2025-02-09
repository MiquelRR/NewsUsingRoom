package com.miquel.newsusingroom.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update


@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE email = :email ")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM user")
    suspend fun getUserList(): MutableList<User>

    @Insert
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

}

@Dao
interface NewsItemDao {
    @Query("SELECT * FROM news_article")
    suspend fun getAllNews(): MutableList<NewsItem>
    @Query("SELECT * FROM news_article WHERE id = :id")
    suspend fun getNewsById(id: Int): NewsItem?
    @Insert
    suspend fun addNewsItem(newsItem: NewsItem)
    @Update
    suspend fun updateNewsItem(newsItem: NewsItem)
    @Delete
    suspend fun deleteNewsItem(newsItem: NewsItem)

}

@Dao
interface LikedDao {
    @Query("SELECT * FROM likes WHERE user_id = :userid")
    suspend fun getLikedNews(userid: Int): List<Liked>
    @Insert
    suspend fun likeNews(liked: Liked)
    @Delete
    suspend fun unlikeNews(liked: Liked)
    @Update
    suspend fun updateLiked(liked: Liked)
}