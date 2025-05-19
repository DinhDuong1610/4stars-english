package com.fourstars.fourstars_english.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fourstars.fourstars_english.R
import com.fourstars.fourstars_english.model.Article
import com.fourstars.fourstars_english.repository.ArticleRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader

class ArticleViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ArticleRepository()



    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadArticles()
    }

    private fun loadArticlesFromRaw() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val context = getApplication<Application>().applicationContext
                val inputStream = context.resources.openRawResource(R.raw.article)
                val json = inputStream.bufferedReader().use(BufferedReader::readText)

                val type = object : TypeToken<List<Article>>() {}.type
                val articleList: List<Article> = Gson().fromJson(json, type)

                _articles.value = articleList
            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    fun loadArticles() {
        viewModelScope.launch {
            _isLoading.value = true
            _articles.value = repository.getArticles()
            _isLoading.value = false
        }
    }

    fun addArticle(article: Article, onDone: () -> Unit) {
        viewModelScope.launch {
            if (repository.addArticle(article)) {
                loadArticles()
                onDone()
            }
        }
    }

    fun updateArticle(article: Article, onDone: () -> Unit) {
        viewModelScope.launch {
            if (repository.updateArticle(article)) {
                loadArticles()
                onDone()
            }
        }
    }

    fun deleteArticle(articleId: String) {
        viewModelScope.launch {
            if (repository.deleteArticle(articleId)) {
                loadArticles()
            }
        }
    }
}