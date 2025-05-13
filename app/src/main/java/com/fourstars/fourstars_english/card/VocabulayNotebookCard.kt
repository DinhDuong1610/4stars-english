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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fourstars.fourstars_english.model.NotebookVocabulary
import com.fourstars.fourstars_english.ui.theme.BeVietnamPro
import com.fourstars.fourstars_english.ui.theme.NotoSansJP
import com.fourstars.fourstars_english.viewModel.TextToSpeechViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VocabularyNotebookCard(
    notebook: NotebookVocabulary,
    onClick: () -> Unit
) {

    val textToSpeechViewModel: TextToSpeechViewModel = viewModel()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notebook.expression,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    fontFamily = NotoSansJP
                )

                // Nút phát âm với nền hình tròn
                Box(
                    modifier = Modifier
                        .size(40.dp)  // Kích thước của nút
                        .clip(CircleShape)  // Hình dạng tròn
                        .background(Color(0xFFEFEFEF))  // Màu nền (màu xám nhạt)
                        .clickable(onClick = {
                            textToSpeechViewModel.speak(notebook.expression)
                        })  // Xử lý sự kiện click
                        .padding(8.dp),  // Khoảng cách giữa icon và viền
                ) {
                    Icon(
                        imageVector = Icons.Outlined.VolumeUp,
                        contentDescription = "Phát âm",
                        tint = Color.Black,  // Màu của icon
                        modifier = Modifier.fillMaxSize(),  // Đảm bảo icon nằm vừa trong vòng tròn
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = notebook.reading,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = notebook.meaning,
                style = TextStyle(fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.Medium, fontFamily = BeVietnamPro),
                fontSize = 18.sp
            )
        }
    }
}

// Hàm format date từ milliseconds
private fun formatDate(millis: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(millis))
}