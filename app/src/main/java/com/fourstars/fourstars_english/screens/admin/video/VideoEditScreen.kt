package com.fourstars.fourstars_english.screens.admin.video

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fourstars.fourstars_english.model.SubtitleLine
import com.fourstars.fourstars_english.model.Video
import com.fourstars.fourstars_english.viewModel.VideoViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoEditScreen(
    navController: NavController,
    videoId: String?,
    viewModel: VideoViewModel = viewModel()
) {
    val videos by viewModel.videoList.collectAsState()
    var video by remember { mutableStateOf<Video?>(null) }

    LaunchedEffect(videoId, videos) {
        if (videos.isNotEmpty()) {
            video = videos.find { it.videoId == videoId }
            if (video == null) {
                Log.d("VideoAdminCard", "Video ID is missing $videoId")
            } else {
                Log.d("VideoAdminCard", "Video found: ${video?.title}")
            }
        }
    }


    var title by remember { mutableStateOf(video?.title ?: "") }
    var duration by remember { mutableStateOf(video?.duration ?: "") }
    var thumbnailUrl by remember { mutableStateOf(video?.thumbnailUrl ?: "") }
    var videoIdInput by remember { mutableStateOf(video?.videoId ?: "") }
    var subtitleJson by remember { mutableStateOf("") }

    val datePosted = video?.datePosted ?: SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())


    // Kiểm tra và cập nhật các input khi video thay đổi
    LaunchedEffect(video) {
        video?.let {
            title = it.title
            duration = it.duration
            thumbnailUrl = it.thumbnailUrl
            videoIdInput = it.videoId
            subtitleJson = Gson().toJson(it.subtitles)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (video == null) "Add Video" else "Edit Video") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title input
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())

            // Duration input
            OutlinedTextField(value = duration, onValueChange = { duration = it }, label = { Text("Duration") }, modifier = Modifier.fillMaxWidth())

            // Thumbnail URL input
            OutlinedTextField(value = thumbnailUrl, onValueChange = { thumbnailUrl = it }, label = { Text("Thumbnail URL") }, modifier = Modifier.fillMaxWidth())

            // Video ID input (added)
            OutlinedTextField(
                value = videoIdInput,
                onValueChange = { videoIdInput = it },
                label = { Text("Video ID") },
                modifier = Modifier.fillMaxWidth()
            )

            // Subtitle JSON input
            OutlinedTextField(
                value = subtitleJson,
                onValueChange = { subtitleJson = it },
                label = { Text("Subtitles JSON") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF2196F3))
            )

            // Save button
            Button(
                onClick = {
                    val gson = Gson()
                    val type = object : TypeToken<List<SubtitleLine>>() {}.type
                    val subtitles: List<SubtitleLine> = try {
                        gson.fromJson(subtitleJson, type)
                    } catch (e: Exception) {
                        emptyList()
                    }

                    val newVideo = Video(
                        title = title,
                        duration = duration,
                        datePosted = datePosted,
                        thumbnailUrl = thumbnailUrl,
                        videoId = videoIdInput.ifEmpty { UUID.randomUUID().toString() }, // Default to generated ID if empty
                        subtitles = subtitles
                    )

                    if (video == null) {
                        viewModel.addVideo(newVideo) { navController.popBackStack() }
                    } else {
                        viewModel.updateVideo(newVideo) { navController.popBackStack() }
                    }
                },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Save")
            }
        }
    }
}

