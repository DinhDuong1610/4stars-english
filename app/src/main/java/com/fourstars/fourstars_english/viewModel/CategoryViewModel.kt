package com.fourstars.fourstars_english.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.fourstars.fourstars_english.model.Category
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CategoryViewModel : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val firestore = FirebaseFirestore.getInstance()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        firestore.collection("cagegories")
            .get()
            .addOnSuccessListener { result ->
                val categoryList = result.mapNotNull { doc ->
                    doc.toObject(Category::class.java)
                }
                _categories.value = categoryList
            }
            .addOnFailureListener { e ->
                Log.e("CategoryVM", "Failed to load categories", e)
            }
    }
}
