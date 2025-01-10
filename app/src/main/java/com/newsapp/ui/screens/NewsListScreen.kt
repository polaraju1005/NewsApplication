package com.newsapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.newsapp.data.model.Article

@Composable
fun NewsListScreen(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit
) {
    LazyColumn {
        items(articles.size) { index ->
            val article = articles[index]
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable { onArticleClick(article) }
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = article.title ?: "No Title")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = article.description ?: "No Description")
                }
            }
        }
    }
}
