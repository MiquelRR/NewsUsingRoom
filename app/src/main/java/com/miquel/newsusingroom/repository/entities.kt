package com.miquel.newsusingroom.repository

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val password: String
) : Serializable

@Entity(tableName = "news_article")
data class NewsItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var link: String,
    var date: String,
    var content: String,
    var author: String? = null,
    var imageUrl: String? = null
)

@Entity(tableName = "likes", primaryKeys = ["user_id", "news_id"])
data class Liked(
    val user_id: Int,
    val news_id: Int
)


