package com.miquel.newsusingroom.repository

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [NewsItem::class, User::class, Liked::class], version = 3, exportSchema = false)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun newsArticleDao(): NewsItemDao
    abstract fun userDao(): UserDao
    abstract fun likedDao(): LikedDao
}