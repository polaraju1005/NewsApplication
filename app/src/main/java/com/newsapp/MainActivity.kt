package com.newsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.newsapp.data.model.Article
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

                NewsListScreen(
                    articles = articles.value,
                    isLoading = viewModel.isLoading,
                    onArticleClick = { article -> openArticleInBrowser(article) },
                    onShareClick = { article -> shareArticle(article) }
                ) { category, country ->
                    fetchCategoryNews(viewModel, category, country)
                }
            }
        }
    }

    private fun openArticleInBrowser(article: Article) {
        val url = article.url
        if (!url.isNullOrEmpty()) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            try {
                startActivity(browserIntent)
            } catch (e: Exception) {
                showToast("No browser app installed!")
            }
        } else {
            showToast("Invalid URL!")
        }
    }

    private fun shareArticle(article: Article) {
        val url = article.url
        val title = article.title
        if (!url.isNullOrEmpty() && !title.isNullOrEmpty()) {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "$title\n\n$url")
                type = "text/plain"
            }
            try {
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            } catch (e: Exception) {
                showToast("Share failed!")
            }
        } else {
            showToast(if (title.isNullOrEmpty()) "Title is missing!" else "URL is missing!")
        }
    }

    private fun fetchCategoryNews(viewModel: NewsViewModel, category: String, country:String) {
        viewModel.fetchNews(
            category = category,
            language = viewModel.selectedLanguage,
            country = country,
            apiKey = BuildConfig.NEWS_API_KEY
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
