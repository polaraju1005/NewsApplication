package com.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newsapp.ui.screens.NewsListScreen
import com.newsapp.ui.theme.NewsAppTheme
import com.newsapp.ui.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                val viewModel: NewsViewModel = viewModel()
                val articles = viewModel.articles.collectAsState(initial = emptyList())

                NewsListScreen(articles = articles.value) { article ->
                    // Handle article click
                }

                viewModel.fetchNews(country = "us", apiKey = BuildConfig.NEWS_API_KEY)
            }
        }
    }
}
