package com.miquel.newsusingroom.repository

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val user_id: Int = 0,
    val email: String,
    val password: String
) : Serializable

@Entity(tableName = "news_article")
data class NewsItem(
    @PrimaryKey(autoGenerate = true)
    val news_id: Int = 0,
    var title: String,
    var link: String,
    var date: String,
    var content: String,
    var author: String? = null,
    var imageUrl: String? = null
)

@Entity(    tableName = "likes",
    primaryKeys = ["user_id", "news_id"],
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["user_id"], childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = NewsItem::class, parentColumns = ["news_id"], childColumns = ["news_id"],
            onDelete = ForeignKey.CASCADE)
    ])
data class Liked(
    val user_id: Int,
    val news_id: Int
)

data class UserWithLikedNews(
    @Embedded val user: User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "news_id",
        associateBy = Junction(Liked::class),
    )
    val likedNews: List<NewsItem>
)


