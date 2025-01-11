package com.newsapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.newsapp.data.model.Article
import com.newsapp.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {
    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    var selectedLanguage by mutableStateOf("en")
    var isLoading by mutableStateOf(false)

    fun fetchNews(category: String, language: String, country: String, apiKey: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = repository.getTopHeadlines(category, language, country, apiKey)
                _articles.value = response.articles
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading = false
            }
        }
    }

    fun convertArticleToJson(article: Article): String {
        return Gson().toJson(article)
    }

}

