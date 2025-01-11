package com.newsapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.newsapp.data.model.Article
import com.newsapp.ui.screens.NewsListScreen
import com.newsapp.ui.screens.WebViewScreen
import com.newsapp.ui.theme.NewsAppTheme
import com.newsapp.ui.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                // Navigation controller
                val navController = rememberNavController()

                // Navigation host to manage multiple screens
                NavHost(navController = navController, startDestination = "news_list") {
                    composable("news_list") {
                        NewsListScreenContent(navController)
                    }
                    composable("webview_screen/{url}") { backStackEntry ->
                        val url = backStackEntry.arguments?.getString("url")
                        WebViewScreen(url ?: "")
                    }
                }
            }
        }
    }
}

@Composable
fun NewsListScreenContent(navController: NavController) {
    val viewModel: NewsViewModel = hiltViewModel() // Ensure this fetches updated data
    val articles = viewModel.articles.collectAsState(initial = emptyList()) // Collect articles data

    val context = LocalContext.current

    NewsListScreen(
        articles = articles.value,
        isLoading = viewModel.isLoading,
        onArticleClick = { article ->
            // URL encoding for safe navigation
            val url = Uri.encode(article.url)  // Ensure correct encoding for navigation
            navController.navigate("webview_screen/$url")
        },
        onShareClick = { article ->
            shareArticle(article, context)
        }
    ) { category, country ->
        // Pass the required parameters for fetching news based on category and country
        fetchCategoryNews(viewModel, category, country)
    }
}

fun shareArticle(article: Article, context: Context) {
    val url = article.url
    val title = article.title
    if (!url.isNullOrEmpty() && !title.isNullOrEmpty()) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, "$title\n\n$url")
            type = "text/plain"
        }
        try {
            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
        } catch (e: Exception) {
            showToast("Share failed!", context)
        }
    } else {
        showToast(if (title.isNullOrEmpty()) "Title is missing!" else "URL is missing!", context)
    }
}

private fun showToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private fun fetchCategoryNews(viewModel: NewsViewModel, category: String, country: String) {
    // Calling the ViewModel function to fetch news data by category
    viewModel.fetchNews(
        category = category,
        language = viewModel.selectedLanguage,
        country = country,
        apiKey = BuildConfig.NEWS_API_KEY
    )
}
