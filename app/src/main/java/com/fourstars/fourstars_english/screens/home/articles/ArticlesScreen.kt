package com.fourstars.fourstars_english.screens.home.articles

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fourstars.fourstars_english.ui.theme.BeVietnamPro
import com.fourstars.fourstars_english.ui.theme.Feather
import com.fourstars.fourstars_english.ui.theme.Nunito
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fourstars.fourstars_english.model.Article
import com.fourstars.fourstars_english.viewModel.ArticleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesScreen(navController: NavController, viewModel: ArticleViewModel = viewModel()) {

    val articles = viewModel.articles.collectAsState()

    val isLoading = viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Reading Articles", fontWeight = FontWeight.Bold, fontFamily = Feather) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading.value) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 10.dp, vertical = 0.dp)
                ) {
                    items(articles.value) { article ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(185.dp) // Đặt chiều cao cố định cho tất cả Card
                                .padding(8.dp)
                                .clickable {
                                    navController.navigate("article_detail/${Uri.encode(article.title)}/${Uri.encode(article.publishDate)}" +
                                            "/${Uri.encode(article.content)}/${Uri.encode(article.imageUrl)}/${Uri.encode(article.audioUrl)}")
                                },
                            shape = RoundedCornerShape(10.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                        ) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                AsyncImage(
                                    model = article.imageUrl,
                                    contentDescription = "Article Image",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                // Nội dung bên phải
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = article.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        maxLines = 2, // Giới hạn số dòng
                                        overflow = TextOverflow.Ellipsis // Thêm dấu "..." nếu quá dài
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = article.content ?: "",
                                        fontSize = 14.sp,
                                        fontFamily = BeVietnamPro,
                                        maxLines = 3, // Giới hạn số dòng
                                        overflow = TextOverflow.Ellipsis // Thêm dấu "..." nếu quá dài
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = article.publishDate,
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.align(Alignment.End),
                                        fontFamily = Nunito
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
