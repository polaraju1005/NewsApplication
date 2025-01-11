package com.newsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.data.model.ArticleEntity
import com.newsapp.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(private val repository: ArticleRepository) : ViewModel() {
    fun saveArticle(article: ArticleEntity) {
        viewModelScope.launch {
            repository.saveArticle(article)
        }
    }

    suspend fun isArticleExists(url: String): Boolean {
        return repository.isArticleExists(url)
    }
}
