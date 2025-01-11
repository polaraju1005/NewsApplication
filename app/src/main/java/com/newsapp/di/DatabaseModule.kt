package com.newsapp.di

import android.content.Context
import androidx.room.Room
import com.newsapp.data.repository.ArticleRepository
import com.newsapp.data.room.AppDatabase
import com.newsapp.data.room.ArticleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "news_app.db").build()
    }

    @Provides
    fun provideArticleDao(db: AppDatabase): ArticleDao = db.articleDao()

    @Provides
    @Singleton
    fun provideArticleRepository(articleDao: ArticleDao): ArticleRepository {
        return ArticleRepository(articleDao)
    }
}
