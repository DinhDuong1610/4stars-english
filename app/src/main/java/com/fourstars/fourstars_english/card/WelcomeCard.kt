package com.fourstars.fourstars_english.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fourstars.fourstars_english.ui.theme.BeVietnamPro
import com.fourstars.fourstars_english.ui.theme.Feather
import com.fourstars.fourstars_english.ui.theme.Nunito

@Composable
fun WelcomeCard(userName: String, streakDays: Int, xpPoints: Int, rank: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF58CC02)),
        border = BorderStroke(2.dp, Color(0xFF58CC02)),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Hi, $userName",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = BeVietnamPro,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column() {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🔥", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$streakDays",
                            fontFamily = Feather,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            text = "Day streak",
                            fontFamily = Nunito,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                Column() {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⭐", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$xpPoints",
                            fontFamily = Feather,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            text = "Total XP",
                            fontFamily = Nunito,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                Column() {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("\uD83D\uDEE1\uFE0F", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$rank",
                            fontFamily = Feather,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            text = "Rank",
                            fontFamily = Nunito,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}