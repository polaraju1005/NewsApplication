package com.newsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun fetchNews(country: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTopHeadlines(country, apiKey)
                _articles.value = response.articles
            } catch (e: Exception) {
                // Handle error (e.g., log it or notify the user)
            }
        }
    }
}
