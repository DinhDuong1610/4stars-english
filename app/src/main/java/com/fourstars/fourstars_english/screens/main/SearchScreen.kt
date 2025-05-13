package com.fourstars.fourstars_english.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fourstars.fourstars_english.card.GrammarCard
import com.fourstars.fourstars_english.card.NewWordCard
import com.fourstars.fourstars_english.model.Grammar
import com.fourstars.fourstars_english.model.GrammarExample
import com.fourstars.fourstars_english.ui.theme.Feather
import com.fourstars.fourstars_english.ui.theme.Nunito
import com.fourstars.fourstars_english.viewModel.GrammarViewModel
import com.fourstars.fourstars_english.viewModel.SearchViewModel
import com.fourstars.fourstars_english.viewModel.VocabularyViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(navController: NavController, vocabularyViewModel: VocabularyViewModel = viewModel(),
                 GrammarViewModel: GrammarViewModel = viewModel(), SearchViewModel: SearchViewModel = viewModel()
) {

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Vocabulary") }

    val vocabList = vocabularyViewModel.vocabList.observeAsState(emptyList())
    val grammarList by GrammarViewModel.filteredGrammarList.collectAsState()

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    val searchHistory by rememberUpdatedState(SearchViewModel.searchHistory.collectAsState().value)

    val indicatorOffset by animateDpAsState(
        targetValue = when (selectedCategory) {
            "Vocabulary" -> 0.dp
            else -> 136.dp // 120.dp (width) + 16.dp (spacing)
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    // Fake data
    val history = listOf("水", "いま", "に", "そうです")

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White) // Màu nền áp dụng cho cả padding
            ) {
                TopAppBar(
                    title = { Text("Search", fontWeight = Bold, fontFamily = Feather) },
                    modifier = Modifier.padding(end = 12.dp),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent, // Để màu nền của Box hiển thị
                        titleContentColor = Color.Black // Màu chữ tiêu đề
                    ),
                    actions = {
                        // Action
                    }
                )

            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        content = { paddingValues ->
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
            )
            {
                item {
                    // Kanji / Grammar Toggle
                    Box(modifier = Modifier.wrapContentSize()) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.LightGray.copy(alpha = 0.2f))
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ChoiceChip(
                                "Vocabulary",
                                selectedCategory == "Vocabulary",
                                { selectedCategory = "Vocabulary" }
                            )
                            ChoiceChip(
                                "Grammar",
                                selectedCategory == "Grammar",
                                { selectedCategory = "Grammar" }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Debounce xử lý logic tìm kiếm
                    LaunchedEffect(searchQuery) {
                        snapshotFlow { searchQuery }
                            .debounce(3000) // chỉ tìm sau 300ms không gõ gì
                            .collect { query ->
                                if (query != "" && query !in searchHistory)
                                    SearchViewModel.saveSearchHistory(userId.toString(), query)
                            }
                    }

                    // Search Field
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            if (selectedCategory == "Vocabulary") {
                                vocabularyViewModel.searchVocabulary(it)
                            } else {
                                GrammarViewModel.searchGrammar(it)
                            }
                        },
                        placeholder = {
                            if (selectedCategory == "Vocabulary")
                                Text("金, キン, かね, gold", fontSize = 16.sp, fontFamily = Nunito)
                            else (Text("いつも, always", fontSize = 16.sp, fontFamily = Nunito)) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon"
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        textStyle = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = Nunito
                        ),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (selectedCategory == "Vocabulary" && vocabList.value.isNotEmpty() && searchQuery.isNotEmpty()) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .height(600.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(vocabList.value) { vocab ->
                                NewWordCard(vocab)
                            }
                        }
                    }
                    else if (selectedCategory == "Grammar" && grammarList.isNotEmpty() && searchQuery.isNotEmpty()) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .height(600.dp)
                                .fillMaxWidth()
                        ) {
                            items(grammarList) { grammar ->
                                GrammarCard(grammar)
                            }
                        }
                    }
                }

                item {
                    // 📚 History
                    if (searchHistory.isNotEmpty()) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Search History", fontWeight = Bold, fontFamily = Feather, fontSize = 25.sp)

                            Text(
                                text = "Clear",
                                color = Color(0xFFEF5350), // Màu xanh nước biển
                                fontSize = 16.sp, // Nhỏ hơn tiêu đề
                                fontFamily = Feather,
                                fontWeight = Bold,
                                modifier = Modifier
                                    .clickable { SearchViewModel.clearSearchHistory(userId.toString()) }
                                    .padding(4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        FlowRow(
                            maxLines = 2,
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            searchHistory.forEach { item ->
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            searchQuery = item
                                            if (selectedCategory == "Vocabulary") {
                                                vocabularyViewModel.searchVocabulary(item)
                                            } else {
                                                GrammarViewModel.searchGrammar(item)
                                            }
                                        }
                                        .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(8.dp))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = item,
                                        fontWeight = Bold,
                                        fontSize = 16.sp,
                                        fontFamily = Nunito
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))
                    }
                }

                item {
                    // Kanji
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Vocabulary", fontWeight = Bold, fontFamily = Feather, fontSize = 25.sp)

                        Text(
                            text = "See All",
                            color = Color(0xFF007BFF), // Màu xanh nước biển
                            fontSize = 16.sp, // Nhỏ hơn tiêu đề
                            fontFamily = Feather,
                            fontWeight = Bold,
                            modifier = Modifier
                                .clickable { navController.navigate("Vocabulary") }
                                .padding(4.dp) // Khoảng cách để dễ nhấn
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
//                    LazyVerticalGrid(
//                        columns = GridCells.Fixed(2),
//                        verticalArrangement = Arrangement.spacedBy(12.dp),
//                        horizontalArrangement = Arrangement.spacedBy(12.dp),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .heightIn(max = 520.dp)
//                    ) {
//                        items(popularKanjis) { kanji ->
//                            KanjiCard(kanji)
//                        }
//                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Grammar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Grammar", fontWeight = Bold, fontFamily = Feather, fontSize = 25.sp)

                        Text(
                            text = "See All",
                            color = Color(0xFF007BFF), // Màu xanh nước biển
                            fontSize = 16.sp, // Nhỏ hơn tiêu đề
                            fontFamily = Feather,
                            fontWeight = Bold,
                            modifier = Modifier
                                .clickable { navController.navigate("grammar") }
                                .padding(4.dp) // Khoảng cách để dễ nhấn
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
//                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//                        popularGrammar.forEach { grammar ->
//                            GrammarCard(grammar)
//                        }
//                    }
                }
            }
        }
    )


}

@Composable
fun ChoiceChip(
    label: String,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animation cho màu nền
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) Color(0xFF43A047) else Color.White,
        animationSpec = tween(durationMillis = 300)
    )

    // Animation cho màu chữ
    val textColor by animateColorAsState(
        targetValue = if (selected) Color.White else Color.Black,
        animationSpec = tween(durationMillis = 300)
    )

    // Animation cho scale khi chọn
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(50.dp))
            .background(backgroundColor)
            .clickable { onSelected() }
            .width(120.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = textColor,
            fontFamily = Nunito,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
    }
}
