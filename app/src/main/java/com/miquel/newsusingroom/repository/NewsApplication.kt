package com.miquel.newsusingroom.repository
import android.app.Application
import androidx.room.Room

class NewsApplication : Application() {
    companion object {
        lateinit var database: NewsDatabase
    }
    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            NewsDatabase::class.java,
            "newsDatabase")
            .fallbackToDestructiveMigration()
            .build()
    }
}