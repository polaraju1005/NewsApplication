package com.newsapp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.newsapp.data.model.Article
import com.newsapp.data.model.ArticleEntity

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleEntity)

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<ArticleEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM articles WHERE url = :url)")
    suspend fun isArticleExists(url: String): Boolean

    @Query("DELETE FROM articles WHERE url = :url")
    suspend fun deleteArticleByUrl(url: String)
}
