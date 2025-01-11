package com.newsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.material.placeholder
import com.newsapp.data.model.Article
import com.newsapp.data.model.ArticleEntity
import com.newsapp.ui.components.BottomNavigationBar
import com.newsapp.ui.viewmodel.NewsViewModel

@Composable
fun NewsListScreen(
    articles: List<Article>,
    isLoading: Boolean,
    onArticleClick: (Article) -> Unit,
    onShareClick: (Article) -> Unit,
    viewModel: NewsViewModel,
    onCategorySelect: (String, String) -> Unit
) {
    val shouldShowBottomBar = true
    var selectedItem by remember { mutableIntStateOf(0) }
    var savedArticles by remember { mutableStateOf<List<ArticleEntity>>(emptyList()) }
    val availableCategories =
        listOf("business", "entertainment", "general", "health", "science", "sports", "technology")
    var selectedCategory by remember { mutableStateOf("business") }
    val selectedCountry = "us"

    LaunchedEffect(selectedCategory, selectedCategory) {
        onCategorySelect(selectedCategory, selectedCountry)
    }

    LaunchedEffect(selectedItem) {
        if (selectedItem == 1) {
            viewModel.fetchSavedArticles()
        }
    }

    savedArticles = viewModel.savedArticles.value

    Box(
        modifier = Modifier
            .background(Color(0xFFF4F4F4))
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFFF4F4F4))
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 56.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Filter Chips for Categories
            if (selectedItem == 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    availableCategories.forEach { category ->
                        FilterChip(text = category.capitalize(),
                            selected = selectedCategory == category,
                            onClick = {
                                selectedCategory = category
                                onCategorySelect(category, selectedCountry)
                            })
                    }
                }
            }

            if (isLoading) {
                repeat(5) {
                    ShimmerArticleCard()
                }
            } else {
                if (selectedItem == 0) {
                    articles.forEach { article ->
                        ArticleCard(
                            article,
                            onArticleClick,
                            onShareClick,
                            showDeleteIcon = false,
                            onDeleteClick = {})
                    }
                } else {
                    if (savedArticles.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "No saved articles yet",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        savedArticles.forEach { article ->
                            ArticleCard(
                                article = Article(
                                    title = article.title,
                                    description = article.description,
                                    url = article.url,
                                    urlToImage = article.urlToImage,
                                    content = article.content
                                ),
                                onArticleClick = onArticleClick,
                                onShareClick = onShareClick,
                                showDeleteIcon = true,
                                onDeleteClick = { articleToDelete ->
                                    viewModel.deleteArticle(articleToDelete.url.toString())
                                })
                        }
                    }
                }
            }


        }

        BottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedItem = selectedItem,
            onItemSelected = { index -> selectedItem = index },
            shouldShow = shouldShowBottomBar
        )
    }
}

@Composable
fun ArticleCard(
    article: Article,
    onArticleClick: (Article) -> Unit,
    onShareClick: (Article) -> Unit,
    showDeleteIcon: Boolean = false,  // Add this parameter
    onDeleteClick: (Article) -> Unit // Add this callback
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { onArticleClick(article) },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {  // Box to overlay the delete icon
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = article.urlToImage ?: "",
                    contentDescription = "Article Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = article.title ?: "No Title",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = article.description ?: "No Description",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Read More...",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onArticleClick(article) })
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Share Icon
                        IconButton(
                            onClick = { onShareClick(article) }, modifier = Modifier.padding(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Share",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        // Delete Icon (only visible if showDeleteIcon is true)
                        if (showDeleteIcon) {
                            IconButton(
                                onClick = { onDeleteClick(article) },
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ShimmerArticleCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray)
                    .placeholder(visible = true)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
                    .placeholder(visible = true)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
                    .placeholder(visible = true)
            )
        }
    }
}

@Composable
fun FilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(modifier = Modifier
        .background(
            if (selected) Color(0xFFB4B4B4) else Color.White, RoundedCornerShape(16.dp)
        )
        .clickable { onClick() }
        .padding(horizontal = 12.dp, vertical = 8.dp)) {
        Text(
            text = text, fontSize = 12.sp, color = if (selected) Color.White else Color.Gray
        )
    }
}
