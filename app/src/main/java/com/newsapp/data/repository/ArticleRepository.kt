package com.newsapp.data.repository

import com.newsapp.data.model.ArticleEntity
import com.newsapp.data.room.ArticleDao
import javax.inject.Inject

class ArticleRepository @Inject constructor(private val articleDao: ArticleDao) {

    suspend fun saveArticle(article: ArticleEntity) {
        articleDao.insertArticle(article)
    }

    suspend fun isArticleExists(url: String): Boolean {
        return articleDao.isArticleExists(url)
    }

    suspend fun getAllArticles(): List<ArticleEntity> {
        return articleDao.getAllArticles()
    }

    suspend fun deleteArticleByUrl(url: String) {
        articleDao.deleteArticleByUrl(url)
    }
}
