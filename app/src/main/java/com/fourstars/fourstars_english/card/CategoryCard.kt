package com.fourstars.fourstars_english.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fourstars.fourstars_english.model.Category
import com.fourstars.fourstars_english.ui.theme.Feather
import com.fourstars.fourstars_english.ui.theme.Nunito

@Composable
fun CategoryCard(navController : NavController, category: Category) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .clickable { navController.navigate("vocabulary/${category.name}") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Nền trắng cho card
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Emoji ở góc trên bên trái
            Text(
                text = category.emoji,
                fontSize = 32.sp, // Icon to hơn và rõ ràng
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart)
            )

            // Nút ba chấm ở góc trên bên phải
            IconButton(
                onClick = { /* Mở menu chỉnh sửa */ },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.MoreHoriz, contentDescription = "Edit")
            }

            // Nội dung Category
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = category.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Feather
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "${category.wordCount} words",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontFamily = Nunito
                )
            }
        }
    }
}