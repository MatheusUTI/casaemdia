package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.AppCode
import com.example.data.AppNote
import com.example.data.DocumentItem
import com.example.data.MaintenanceItem
import com.example.data.MaintenanceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MaintenanceRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = MaintenanceRepository(database.maintenanceDao())
        viewModelScope.launch {
            repository.prepopulateIfEmpty()
        }
    }

    // Streams of data
    val items: StateFlow<List<MaintenanceItem>> = repository.allItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val codes: StateFlow<List<AppCode>> = repository.allCodes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notes: StateFlow<List<AppNote>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val documents: StateFlow<List<DocumentItem>> = repository.allDocuments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search query for Archive tab
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Add item functions
    fun addMaintenanceItem(title: String, category: String, subtitle: String?, daysLeft: Int, notes: String?, smartReminder: Boolean) {
        viewModelScope.launch {
            val item = MaintenanceItem(
                title = title,
                category = category,
                subtitle = subtitle ?: category,
                daysLeft = daysLeft,
                isCompleted = false,
                notes = notes,
                smartReminder = smartReminder
            )
            repository.insertItem(item)
        }
    }

    fun completeItem(itemId: Int) {
        viewModelScope.launch {
            val currentItems = items.value
            val item = currentItems.find { it.id == itemId }
            if (item != null) {
                // Get month & year or simplified Portugese date for completed action
                val completedItem = item.copy(
                    isCompleted = true,
                    completedDateStr = "Hoje",
                    daysLeft = item.daysLeft + 60 // moves it further in timeline
                )
                repository.updateItem(completedItem)
            }
        }
    }

    fun addNote(text: String, dateStr: String) {
        viewModelScope.launch {
            repository.insertNote(AppNote(text = text, dateStr = dateStr))
        }
    }

    fun addCode(title: String, value: String, iconName: String) {
        viewModelScope.launch {
            repository.insertCode(AppCode(title = title, value = value, iconName = iconName))
        }
    }

    fun addDocument(fileName: String, fileSize: String, fileType: String) {
        viewModelScope.launch {
            repository.insertDocument(DocumentItem(fileName = fileName, fileSize = fileSize, fileType = fileType))
        }
    }
}
