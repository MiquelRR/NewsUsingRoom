package com.miquel.newsusingroom.entities

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