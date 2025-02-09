package com.miquel.newsusingroom.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "likes", primaryKeys = ["user_id", "news_id"],
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"]),
        ForeignKey(entity = NewsItem::class, parentColumns = ["id"], childColumns = ["news_id"])
    ])
data class Liked(
    val user_id: Int,
    val news_id: Int
)
