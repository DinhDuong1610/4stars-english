package com.fourstars.fourstars_english.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fourstars.fourstars_english.R
import com.fourstars.fourstars_english.model.Vocabulary
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class VocabularyViewModel(application: Application) : AndroidViewModel(application) {

    private val _vocabList = MutableLiveData<List<Vocabulary>>()
    val vocabList: LiveData<List<Vocabulary>> = _vocabList

    private lateinit var fullList: List<Vocabulary>

    fun loadAllVocabulary(onLoaded: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!::fullList.isInitialized) {
                try {
                    val inputStream = getApplication<Application>().resources.openRawResource(R.raw.vocab)
                    val reader = InputStreamReader(inputStream)
                    val type = object : TypeToken<Map<String, Vocabulary>>() {}.type
                    val vocabMap: Map<String, Vocabulary> = Gson().fromJson(reader, type)

                    fullList = vocabMap.values.toList()

                    withContext(Dispatchers.Main) {
                        _vocabList.value = fullList
                        onLoaded()
                    }
                } catch (e: Exception) {
                    Log.e("VocabVM", "Failed to load vocab.json", e)
                }
            } else {
                withContext(Dispatchers.Main) {
                    onLoaded()
                }
            }
        }
    }

    fun loadRandomFive() {
        if (::fullList.isInitialized) {
            val first100 = fullList.take(100)
            _vocabList.value = first100.shuffled().take(5)
        }
    }

    fun searchVocabulary(keyword: String) {
        if (!::fullList.isInitialized) {
            println("Warning: Vocabulary not loaded!")
            _vocabList.value = emptyList()
            return
        }

        val result = fullList.filter { entry ->
            entry.word.contains(keyword.trim(), ignoreCase = true)
        }

        println("[DEBUG] Found ${result.size} results for keyword '$keyword'")
        _vocabList.value = result
    }

    fun searchWord(keyword: String): Vocabulary? {
        if (!::fullList.isInitialized) {
            println("Warning: Vocabulary not loaded!")
            _vocabList.value = emptyList()
            return null
        }

        val result = fullList.filter { entry ->
            entry.word.contains(keyword.trim(), ignoreCase = true)
        }

        println("[DEBUG] Found ${result.size} results for keyword '$keyword'")
        _vocabList.value = result
        return result.firstOrNull()
    }

    fun filterByCategory(category: String): List<Vocabulary> {
        return if (::fullList.isInitialized) {
            fullList.filter { it.category.equals(category, ignoreCase = true) }
        } else {
            emptyList()
        }
    }

    fun getAllCategories(): List<String> {
        return if (::fullList.isInitialized) {
            fullList.map { it.category }.distinct().sorted()
        } else {
            emptyList()
        }
    }
}
