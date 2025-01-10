package com.newsapp.data.repository

import com.newsapp.data.api.NewsApiService
import com.newsapp.data.model.NewsResponse
import com.newsapp.domain.repository.NewsRepository

class NewsRepositoryImpl(private val apiService: NewsApiService) : NewsRepository {
    override suspend fun getTopHeadlines(country: String, apiKey: String): NewsResponse {
        return apiService.getTopHeadlines(country, apiKey)
    }
}