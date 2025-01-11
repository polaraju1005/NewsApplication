package com.newsapp.ui.screens

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.google.gson.Gson
import com.newsapp.data.model.Article
import com.newsapp.data.model.ArticleEntity
import com.newsapp.ui.viewmodel.WebViewViewModel
import kotlinx.coroutines.launch

@Composable
fun WebViewScreen(backStackEntry: NavBackStackEntry) {
    val articleJson = backStackEntry.arguments?.getString("article")
    val article = Gson().fromJson(articleJson, Article::class.java)

    val context = LocalContext.current
    val webView = remember { WebView(context) }
    val viewModel: WebViewViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()

    webView.apply {
        settings.javaScriptEnabled = true
        webViewClient = WebViewClient()
        webChromeClient = WebChromeClient()
        article.url?.let { loadUrl(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
            factory = { webView },
            update = { article.url?.let { it1 -> it.loadUrl(it1) } })

        Button(
            onClick = {
                val entity = article.title?.let { title ->
                    article.url?.let { url ->
                        ArticleEntity(
                            title = title,
                            description = article.description,
                            url = url,
                            urlToImage = article.urlToImage,
                            content = article.content
                        )
                    }
                }
                entity?.let {
                    coroutineScope.launch {
                        val message = saveArticle(viewModel, it)
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }, shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary, contentColor = Color.Black
            ), modifier = Modifier
                .padding(vertical = 16.dp)
                .height(50.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Save Article",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private suspend fun saveArticle(viewModel: WebViewViewModel, article: ArticleEntity): String {
    return try {
        val isDuplicate =
            viewModel.isArticleExists(article.url)
        if (isDuplicate) {
            "Article already saved."
        } else {
            viewModel.saveArticle(article)
            "Article saved successfully."
        }
    } catch (e: Exception) {
        "Error saving article: ${e.message}"
    }
}