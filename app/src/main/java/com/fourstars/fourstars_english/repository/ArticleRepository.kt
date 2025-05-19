package com.fourstars.fourstars_english.repository

import com.fourstars.fourstars_english.model.Article
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ArticleRepository {
    private val db = FirebaseFirestore.getInstance()
    private val articlesCollection = db.collection("articles")

    suspend fun getArticles(): List<Article> {
        return try {
            val snapshot = articlesCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Article::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addArticle(article: Article): Boolean {
        return try {
            val docRef = articlesCollection.add(article.copy(id = "")).await()
            val generatedId = docRef.id

            articlesCollection.document(generatedId)
                .update("id", generatedId)
                .await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateArticle(article: Article): Boolean {
        return try {
            articlesCollection.document(article.id).set(article).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteArticle(articleId: String): Boolean {
        return try {
            articlesCollection.document(articleId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
