package com.miquel.newsusingroom.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.miquel.newsusingroom.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE email = :email ")
    suspend fun getUserByEmail(email: String): User?
    @Query("SELECT * FROM user")
    suspend fun getUserList():  MutableList<User>
    @Insert
    suspend fun addUser(user: User)
    @Update
    suspend fun updateUser(user: User)
    @Delete
    suspend fun deleteUser(user: User)
}