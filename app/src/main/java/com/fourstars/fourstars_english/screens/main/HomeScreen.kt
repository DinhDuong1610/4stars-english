package com.fourstars.fourstars_english.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.fourstars.fourstars_english.repository.AuthRepository
import com.fourstars.fourstars_english.viewModel.ArticleViewModel
import com.fourstars.fourstars_english.viewModel.VocabularyViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fourstars.fourstars_english.card.CategoryCard
import com.fourstars.fourstars_english.card.NewWordCard
import com.fourstars.fourstars_english.card.ReadingCard
import com.fourstars.fourstars_english.card.VideoCard
import com.fourstars.fourstars_english.card.WelcomeCard
import com.fourstars.fourstars_english.model.BottomNavItem
import com.fourstars.fourstars_english.model.Category
import com.fourstars.fourstars_english.ui.theme.Feather
import com.fourstars.fourstars_english.ui.theme.Nunito
import com.fourstars.fourstars_english.viewModel.CategoryViewModel
import com.fourstars.fourstars_english.viewModel.VideoViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, authRepo: AuthRepository,
               viewModel: ArticleViewModel = viewModel(), viewModel1: VocabularyViewModel = viewModel(), videoViewModel: VideoViewModel = viewModel()) {

    val user = authRepo.getCurrentUser()
    val articles = viewModel.articles.collectAsState()
    val vocabList = viewModel1.vocabList.observeAsState(emptyList())
    val videos = videoViewModel.videoList

    val categoryViewModel: CategoryViewModel = viewModel()
    val categories = categoryViewModel.categories.collectAsState()



    LaunchedEffect(Unit) {
        if (vocabList.value.isEmpty()) {
            viewModel1.loadAllVocabulary {
                viewModel1.loadRandomFive()
            }
        } else {
            viewModel1.loadRandomFive()
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                TopAppBar(
                    title = { Text("4Stars", fontWeight = FontWeight.Bold, fontFamily = Feather) },
                    modifier = Modifier.padding(end = 12.dp),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.Black
                    ),
                    actions = {
                        BadgedBox(
                            badge = {
                                val notificationCount = 3
                                if (notificationCount > 0) {
                                    Badge(
                                        modifier = Modifier.offset(x = (-16).dp, y = 7.dp),
                                    ) { Text(notificationCount.toString()) }
                                }
                            }
                        ) {
                            IconButton(onClick = {
                                navController.navigate("notification")
                            }) {
                                Icon(Icons.Default.Notifications, contentDescription = "Thông báo")
                            }
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        IconButton(onClick = {
                            navController.navigate("settings")
                        }) {
                            Icon(Icons.Default.Settings, contentDescription = "Cài đặt")
                        }


                        Spacer(modifier = Modifier.width(15.dp))


                        user?.photoUrl?.let { avatarUrl ->
                            AsyncImage(
                                model = avatarUrl,
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .size(35.dp)
                                    .clip(CircleShape)
                                    .clickable { navController.navigate("profile") }
                            )
                        }
                    }
                )

            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))

                    val userName = user.displayName
                    val streak = remember { (1..30).random() }
                    val xp = remember { (100..1000).random() }
                    val rank = "Silver"

                    WelcomeCard(userName = userName.toString(), streakDays = streak, xpPoints = xp, rank = rank)

                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    // Category - Từ vựng theo chủ đề
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Category",
                            style = TextStyle(fontFamily = Feather, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        )

                        Text(
                            text = "See All",
                            color = Color(0xFF007BFF),
                            fontSize = 16.sp,
                            fontFamily = Feather,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { navController.navigate("category") }
                                .padding(4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    LazyRow(
                        Modifier.height(150.dp)
                    ) {
                        items(categories.value) { category ->
                            CategoryCard(navController, category)
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))
                }

                item {
                    // New Words - Từ vựng mới
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Vocabulary",
                            style = TextStyle(fontFamily = Feather, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        )

                        Text(
                            text = "See All",
                            color = Color(0xFF007BFF), // Màu xanh nước biển
                            fontSize = 16.sp, // Nhỏ hơn tiêu đề
                            fontFamily = Feather,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { navController.navigate("vocabulary/${"All"}") }
                                .padding(4.dp) // Khoảng cách để dễ nhấn
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }

                // Danh sách từ vựng
                items(vocabList.value) { vocab ->
                    NewWordCard(vocab)
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    // Tiêu đề bài đọc
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Reading articles",
                            style = TextStyle(fontFamily = Feather, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        )

                        Text(
                            text = "See All",
                            color = Color(0xFF007BFF), // Màu xanh nước biển
                            fontSize = 16.sp, // Nhỏ hơn tiêu đề
                            fontFamily = Feather,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { navController.navigate("articles") }
                                .padding(4.dp) // Khoảng cách để dễ nhấn
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    // Danh sách bài đọc
                    LazyRow (
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(articles.value) { article ->
                            ReadingCard(navController, article)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    // Tiêu đề bài đọc
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Watch video",
                            style = TextStyle(fontFamily = Feather, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        )

                        Text(
                            text = "See All",
                            color = Color(0xFF007BFF), // Màu xanh nước biển
                            fontSize = 16.sp, // Nhỏ hơn tiêu đề
                            fontFamily = Feather,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("videos")
                                }
                                .padding(4.dp) // Khoảng cách để dễ nhấn
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(videos.value) { video ->
                            VideoCard(navController, video)
                        }
                    }
                }
            }
        }

    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("home", "Home", Icons.Default.Home, Icons.Outlined.Home),
        BottomNavItem("search", "Search", Icons.Default.Translate, Icons.Outlined.Translate),
        BottomNavItem("learning", "Study", Icons.Default.School, Icons.Outlined.School),
        BottomNavItem("community", "Community", Icons.Default.People, Icons.Outlined.People)
    )

    NavigationBar(
        containerColor = Color(0x8BF5F5F5),
    ) {
        val currentRoute = navController.currentDestination?.route
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label, fontFamily = Nunito) },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xB4B3E5FC),
                ),
            )
        }
    }
}



