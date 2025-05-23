package com.fourstars.fourstars_english.screens.study

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fourstars.fourstars_english.viewModel.FlashcardViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fourstars.fourstars_english.ui.theme.Feather
import com.fourstars.fourstars_english.ui.theme.NotoSansJP
import com.fourstars.fourstars_english.ui.theme.Nunito

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(navController: NavHostController, notebookId: String, viewModel: FlashcardViewModel = viewModel()) {
    val flashcards by viewModel.flashcards.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val isFlipped by viewModel.isFlipped.collectAsState()
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(400)
    )

    LaunchedEffect(notebookId) {
        viewModel.loadFlashcards(notebookId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Flashcards",
                        fontWeight = FontWeight.Bold,
                        fontFamily = Feather
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 150.dp)
        ) {
            if (flashcards.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No flashcards available.")
                }
            } else {
                val currentCard = flashcards[currentIndex]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.height(40.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clickable {
                                viewModel.flipCard()
                            }
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = 8f * density
                            },
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier.graphicsLayer {
                                    rotationY = if (rotation > 90f) 180f else 0f
                                }
                            ) {
                                if (rotation < 90f) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = currentCard.expression,
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = NotoSansJP
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "(${currentCard.reading})",
                                            fontSize = 20.sp,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                } else {
                                    Text(currentCard.meaning, fontSize = 25.sp, fontFamily = Nunito)
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Nút Previous với icon
                        IconButton(
                            onClick = { viewModel.previousCard() },
                            modifier = Modifier
                                .size(45.dp)
                                .background(
                                    color = if (currentIndex > 0) Color(0xFF43A047) else Color.LightGray,
                                    shape = RoundedCornerShape(15.dp)
                                ),
                            enabled = currentIndex > 0
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Previous",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Button(
                            onClick = { viewModel.shuffleFlashcards() },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2196F3)
                            )
                        ) {
                            Text("Shuffle", fontFamily = Feather, fontSize = 18.sp)
                        }

                        IconButton(
                            onClick = { viewModel.nextCard() },
                            modifier = Modifier
                                .size(45.dp)
                                .background(
                                    color = if (currentIndex < flashcards.lastIndex) Color(0xFF43A047) else Color.LightGray,
                                    shape = RoundedCornerShape(15.dp)
                                ),
                            enabled = currentIndex < flashcards.lastIndex
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowForward,
                                contentDescription = "Next",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}