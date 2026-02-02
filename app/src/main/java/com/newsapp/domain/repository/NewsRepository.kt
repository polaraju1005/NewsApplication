package com.newsapp.domain.repository

import com.newsapp.data.model.NewsResponse

interface NewsRepository {
    suspend fun getTopHeadlines(category: String, language: String, country: String, apiKey: String): NewsResponse
}