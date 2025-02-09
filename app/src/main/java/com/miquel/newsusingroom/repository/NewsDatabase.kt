package com.miquel.newsusingroom.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.miquel.newsusingroom.entities.Liked
import com.miquel.newsusingroom.entities.NewsItem
import com.miquel.newsusingroom.entities.User

@Database(entities = [NewsItem::class, User::class, Liked::class], version = 1, exportSchema = false)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun newsArticleDao(): NewsItemDao
    abstract fun userDao(): UserDao
    abstract fun likedDao(): LikedDao
}