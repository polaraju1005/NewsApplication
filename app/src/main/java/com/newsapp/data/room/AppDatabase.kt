package com.newsapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.newsapp.data.model.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}
