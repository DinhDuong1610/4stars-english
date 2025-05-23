package com.fourstars.fourstars_english.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fourstars.fourstars_english.model.Grammar
import com.fourstars.fourstars_english.ui.theme.BeVietnamPro
import com.fourstars.fourstars_english.ui.theme.Feather
import com.fourstars.fourstars_english.ui.theme.NotoSansJP

@Composable
fun GrammarCard(grammar: Grammar) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = grammar.phrase,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Feather
                )

//                Box(
//                    modifier = Modifier
//                        .background(getJlptColor(grammar.jlptLevel), shape = RoundedCornerShape(20.dp))
//                        .padding(horizontal = 10.dp, vertical = 5.dp)
//                ) {
//                    Text(
//                        text = grammar.jlptLevel,
//                        style = TextStyle(
//                            fontSize = 12.sp,
//                            color = Color.White,
//                            fontWeight = FontWeight.Medium,
//                            fontFamily = Feather
//                        ),
//                    )
//                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = grammar.meaning,
                fontSize = 18.sp,
                fontFamily = BeVietnamPro
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Structure:",
                color = Color(0xFF0091EA),
                fontSize = 16.sp,
                fontFamily = BeVietnamPro,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = grammar.structure,
                fontSize = 18.sp,
                fontFamily = NotoSansJP,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Meaning:",
                color = Color(0xFF0091EA),
                fontSize = 16.sp,
                fontFamily = BeVietnamPro,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = grammar.explanation,
                fontSize = 15.sp,
                fontFamily = BeVietnamPro,
                lineHeight = 20.sp,
                textAlign = TextAlign.Justify,
            )

            Spacer(modifier = Modifier.height(5.dp))

            Spacer(Modifier.height(6.dp))
            grammar.examples.forEach { example ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(example.sentence, fontSize = 16.sp, fontFamily = NotoSansJP)
                    Spacer(Modifier.height(3.dp))
                    Text(example.translation, fontSize = 15.sp, color = Color.Gray, fontFamily = BeVietnamPro)
                }
            }
        }
    }
}