package com.miquel.newsusingroom.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.miquel.newsusingroom.entities.Liked


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
