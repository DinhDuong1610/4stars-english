package com.fourstars.fourstars_english.card

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fourstars.fourstars_english.model.Vocabulary
import com.fourstars.fourstars_english.viewModel.TextToSpeechViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fourstars.fourstars_english.ui.theme.BeVietnamPro
import com.fourstars.fourstars_english.ui.theme.Feather
import com.fourstars.fourstars_english.ui.theme.NotoSansJP
import com.fourstars.fourstars_english.ui.theme.Nunito

@Composable
fun VocabularyDetailCard(
    vocab: Vocabulary,
    modifier: Modifier = Modifier,
    ttsViewModel: TextToSpeechViewModel = viewModel()
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Ảnh minh họa
            AsyncImage(
                model = vocab.image,
                contentDescription = "Image of ${vocab.word}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(15.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Từ vựng + Nút phát âm
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = vocab.word,
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = NotoSansJP
                        )
                    )
                    Text(
                        text = vocab.ipa,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontFamily = BeVietnamPro
                        )
                    )
                }

                // Nút phát âm với nền hình tròn
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFEFEF))
                        .clickable(onClick = {
                            ttsViewModel.speak(vocab.word)
                        })
                        .padding(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.VolumeUp,
                        contentDescription = "Phát âm",
                        tint = Color.Black,  // Màu của icon
                        modifier = Modifier.fillMaxSize(),  // Đảm bảo icon nằm vừa trong vòng tròn
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nghĩa & Từ loại
            Text(
                text = "Definition:",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    fontFamily = BeVietnamPro
                )
            )
            Text(
                text = vocab.definition,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = BeVietnamPro
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Translation: ${vocab.translation}",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = BeVietnamPro,
                    color = Color(0xFF4CAF50) // xanh lá nhẹ
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category + Part of Speech
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF66BB6A), shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = vocab.category,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.White,
                            fontFamily = Feather
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .background(Color(0xFF03A9F4), shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = vocab.part_of_speech,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.White,
                            fontFamily = BeVietnamPro
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Ví dụ sử dụng
            Text(
                text = "Examples:",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    fontFamily = BeVietnamPro
                )
            )

            vocab.examples.forEach { example ->
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = "• ${example.en}",
                        style = TextStyle(fontSize = 16.sp, fontFamily = Nunito)
                    )
                    Text(
                        text = example.vi,
                        style = TextStyle(fontSize = 15.sp, fontFamily = Nunito, color = Color.Gray)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
