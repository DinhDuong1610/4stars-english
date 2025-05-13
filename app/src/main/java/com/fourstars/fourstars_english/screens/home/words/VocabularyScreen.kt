package com.fourstars.fourstars_english.screens.home.words

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fourstars.fourstars_english.model.NewWord
import com.fourstars.fourstars_english.ui.theme.Feather
import com.fourstars.fourstars_english.viewModel.VocabularyViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fourstars.fourstars_english.card.NewWordCard
import com.fourstars.fourstars_english.ui.theme.Nunito

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyScreen(
    navController: NavController,
    category: String,
    viewModel: VocabularyViewModel = viewModel() // Khởi tạo ViewModel
) {
    var expanded by remember { mutableStateOf(false) } // Trạng thái menu
    var selectedCategory by remember { mutableStateOf(category) } // Category đang được chọn
    var searchText by remember { mutableStateOf("") } // Tìm kiếm

    // Gọi load data khi Composable được khởi tạo
    LaunchedEffect(Unit) {
        viewModel.loadAllVocabulary()
    }

    // Quan sát danh sách từ vựng từ ViewModel
    val vocabList = viewModel.vocabList.observeAsState(emptyList())

    // Lấy danh sách các category từ ViewModel
    val categoryOptions = remember(vocabList.value) {
        listOf("All") + viewModel.getAllCategories()
    }

    // UI chính
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vocabulary", fontWeight = FontWeight.Bold, fontFamily = Feather) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Dropdown và ô tìm kiếm
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dropdown Category
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor().width(110.dp),
                        value = selectedCategory,
                        onValueChange = { selectedCategory = it },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        textStyle = TextStyle(fontFamily = Feather, fontSize = 16.sp),
                        maxLines = 1
                    )
                    // Menu item
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categoryOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption, fontFamily = Feather, fontSize = 16.sp) },
                                onClick = {
                                    selectedCategory = selectionOption
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }

                // TextField tìm kiếm
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth(),
                    placeholder = { Text("Find words...", fontFamily = Nunito, fontSize = 18.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Tìm kiếm") },
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = Nunito, fontSize = 18.sp),
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Lọc danh sách theo category và searchText
            val searchWord = vocabList.value.filter { word ->
                (selectedCategory == "All" || word.category.equals(selectedCategory, ignoreCase = true)) &&
                        (searchText.isEmpty() ||
                                word.definition.contains(searchText, ignoreCase = true) ||
                                word.word.contains(searchText, ignoreCase = true))
            }

            // Hiển thị danh sách từ vựng
            LazyColumn {
                items(searchWord) { word ->
                    NewWordCard(word)
                }
            }
        }
    }
}

