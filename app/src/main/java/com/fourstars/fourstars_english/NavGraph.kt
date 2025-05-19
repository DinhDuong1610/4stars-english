package com.fourstars.fourstars_english

import android.os.Build
import android.window.SplashScreen
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fourstars.fourstars_english.repository.AuthRepository
import com.fourstars.fourstars_english.repository.GoogleAuthRepository
import com.fourstars.fourstars_english.screens.auth.LoginScreen
import com.fourstars.fourstars_english.screens.auth.RegisterScreen
import com.fourstars.fourstars_english.screens.community.UserProfileScreen
import com.fourstars.fourstars_english.screens.home.NotificationScreen
import com.fourstars.fourstars_english.screens.home.SettingsScreen
import com.fourstars.fourstars_english.screens.home.articles.ArticleDetailScreen
import com.fourstars.fourstars_english.screens.home.articles.ArticlesScreen
import com.fourstars.fourstars_english.screens.home.video.VideoScreen
import com.fourstars.fourstars_english.screens.home.words.CategoryScreen
import com.fourstars.fourstars_english.screens.home.words.VocabularyDetailScreen
import com.fourstars.fourstars_english.screens.home.words.VocabularyScreen
import com.fourstars.fourstars_english.screens.main.CommunityScreen
import com.fourstars.fourstars_english.screens.main.HomeScreen
import com.fourstars.fourstars_english.screens.main.LearningScreen
import com.fourstars.fourstars_english.screens.main.SearchScreen
import com.fourstars.fourstars_english.screens.search.GrammarScreen
import com.fourstars.fourstars_english.screens.study.FlashcardScreen
import com.fourstars.fourstars_english.screens.study.NotebookDetailScreen
import com.fourstars.fourstars_english.screens.study.QuizScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController) {
    val authRepo = AuthRepository()
    val context = LocalContext.current
    val googleAuthRepo = GoogleAuthRepository(context)

    // ✅ Trì hoãn xác định startDestination để chờ Firebase đồng bộ
    val startDestination by produceState(initialValue = "splash") {
        delay(500) // Chờ FirebaseAuth cập nhật
        value = if (FirebaseAuth.getInstance().currentUser != null) "home" else "splash"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { LoginScreen(navController, authRepo, googleAuthRepo) }
        composable("register") { RegisterScreen(navController, authRepo, googleAuthRepo) }
        composable("splash") { SplashScreen(navController) }
        composable("greeting") { GreetingScreen(navController) }
        composable("home") { HomeScreen(navController, authRepo) }
        composable("search") { SearchScreen(navController) }
        composable("learning") { LearningScreen(navController) }
        composable("community") { CommunityScreen(navController) }
        composable("category") { CategoryScreen(navController) }
        composable("vocabulary/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            VocabularyScreen(navController, category)
        }
        composable("vocab_detail/{vocabJson}") { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("vocabJson") ?: ""
            VocabularyDetailScreen(
                navController = navController,
                encodedJson = encodedJson
            )
        }
        composable("articles") { ArticlesScreen(navController) }
        composable(
            route = "article_detail/{title}/{publishDate}/{content}/{imageUrl}/{audioUrl}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("publishDate") { type = NavType.StringType },
                navArgument("content") { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType },
                navArgument("audioUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val publishDate = backStackEntry.arguments?.getString("publishDate") ?: ""
            val content = backStackEntry.arguments?.getString("content") ?: ""
            val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
            val audioUrl = backStackEntry.arguments?.getString("audioUrl") ?: ""
            ArticleDetailScreen(title, publishDate, content, imageUrl, audioUrl, navController)
        }
        composable("video/{videoId}") { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId") ?: return@composable
            VideoScreen(navController, videoId = videoId)
        }
        composable("videos") { VideoScreen(navController) }
        composable("grammar") { GrammarScreen(navController) }

        // Notebook routes
        composable(
            route = "notebook_detail/{notebookId}",
            arguments = listOf(
                navArgument("notebookId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val notebookId = backStackEntry.arguments?.getString("notebookId") ?: ""
            NotebookDetailScreen(navController, notebookId)
        }
        composable("flashcard/{notebookId}") { backStackEntry ->
            val notebookId = backStackEntry.arguments?.getString("notebookId") ?: ""
            FlashcardScreen(navController, notebookId)
        }

        composable(
            route = "notebook/{notebookId}/quiz",
            arguments = listOf(navArgument("notebookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val notebookId = backStackEntry.arguments?.getString("notebookId") ?: return@composable
            QuizScreen(
                notebookId = notebookId,
                onBack = { navController.popBackStack() }
            )
        }
        composable("profile") { UserProfileScreen(navController) }
        composable("settings") {
            SettingsScreen(
                navController = navController,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable("notification") { NotificationScreen(navController) }
    }
}
