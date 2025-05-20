package com.fourstars.fourstars_english.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fourstars.fourstars_english.R
import com.fourstars.fourstars_english.model.Video
import com.fourstars.fourstars_english.repository.VideoRepository
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VideoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = VideoRepository()

    var currentVideoId: String = ""
    var currentPosition: Float = 0f
    var currentVideo: Video? = null

    private val _videoList = MutableStateFlow<List<Video>>(emptyList())
    val videoList: StateFlow<List<Video>> = _videoList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadVideos()
    }

    fun loadVideos() {
        viewModelScope.launch {
            _isLoading.value = true
            _videoList.value = repository.getVideos()
            Log.d("VideoViewModel", "Loaded videos: ${_videoList.value.size}")
            _isLoading.value = false
        }
    }

    fun addVideo(video: Video, onDone: () -> Unit) {
        viewModelScope.launch {
            if (repository.addVideo(video)) {
                loadVideos()
                onDone()
            }
        }
    }

    fun updateVideo(video: Video, onDone: () -> Unit) {
        viewModelScope.launch {
            if (repository.updateVideo(video)) {
                loadVideos()
                onDone()
            }
        }
    }

    fun deleteVideo(id: String) {
        viewModelScope.launch {
            if (repository.deleteVideo(id)) {
                loadVideos()
            }
        }
    }

    private fun loadVideosFromJson() {
        val context = getApplication<Application>().applicationContext
        val inputStream = context.resources.openRawResource(R.raw.video) // Đặt file json là res/raw/videos.json
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val gson = Gson()
        val type = object : TypeToken<List<Video>>() {}.type
        _videoList.value = gson.fromJson(jsonString, type)
    }

}