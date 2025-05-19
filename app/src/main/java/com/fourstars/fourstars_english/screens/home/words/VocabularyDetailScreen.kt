package com.fourstars.fourstars_english.screens.home.words

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.fourstars.fourstars_english.card.VocabularyDetail
import com.fourstars.fourstars_english.card.VocabularyDetailCard
import com.fourstars.fourstars_english.model.Vocabulary
import com.fourstars.fourstars_english.ui.theme.Feather
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyDetailScreen(
    navController: NavController,
    encodedJson: String
) {
    val decoded = URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.toString())
    val vocab = Gson().fromJson(decoded, Vocabulary::class.java)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(vocab.word, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )

        }
    ) { padding ->
//        VocabularyDetailCard(
//            vocab = vocab,
//            modifier = Modifier.padding(padding)
//        )
        VocabularyDetail(
            vocab = vocab,
            modifier = Modifier.padding(padding)
        )
    }
}

