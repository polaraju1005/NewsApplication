package com.newsapp.data.repository

import com.newsapp.data.model.ArticleEntity
import com.newsapp.data.room.ArticleDao
import javax.inject.Inject

class ArticleRepository @Inject constructor(private val articleDao: ArticleDao) {

    // Save an article to the database
    suspend fun saveArticle(article: ArticleEntity) {
        articleDao.insertArticle(article)
    }

    // Check if an article exists in the database
    suspend fun isArticleExists(url: String): Boolean {
        return articleDao.isArticleExists(url)
    }
}
