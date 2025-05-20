package com.fourstars.fourstars_english.repository

import com.fourstars.fourstars_english.model.Video
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class VideoRepository {
    private val db = FirebaseFirestore.getInstance()
    private val videosCollection = db.collection("videos")

    suspend fun getVideos(): List<Video> {
        return try {
            val snapshot = videosCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Video::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun addVideo(video: Video): Boolean {
        return try {
            videosCollection.document(video.videoId).set(video).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateVideo(video: Video): Boolean {
        return try {
            videosCollection.document(video.videoId).set(video).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteVideo(id: String): Boolean {
        return try {
            videosCollection.document(id).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
