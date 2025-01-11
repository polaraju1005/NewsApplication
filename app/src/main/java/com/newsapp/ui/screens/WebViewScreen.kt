package com.newsapp.ui.screens

import android.content.Context
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.newsapp.ui.theme.NewsAppTheme

@Composable
fun WebViewScreen(url: String) {
    val context = LocalContext.current
    val webView = remember { WebView(context) }
    webView.apply {
        settings.javaScriptEnabled = true
        webViewClient = WebViewClient()
        webChromeClient = WebChromeClient()
        loadUrl(url)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                webView
            },
            update = { it.loadUrl(url) }
        )

        // Save Article Button
        Button(
            onClick = {
                saveArticle(context, url)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Save Article")
        }
    }
}

private fun saveArticle(context: Context, url: String) {
    try {
        // Imagine saving it in a local database or storage (dummy code here)
        Toast.makeText(context, "Article saved!", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Save failed!", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NewsAppTheme {
        WebViewScreen("https://www.example.com")
    }
}
