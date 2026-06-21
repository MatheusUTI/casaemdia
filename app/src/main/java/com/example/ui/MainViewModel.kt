package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.AppCode
import com.example.data.AppNote
import com.example.data.DocumentItem
import com.example.data.MaintenanceItem
import com.example.data.HistoryEntryEntity
import com.example.data.MaintenanceRepository
import com.example.data.mapper.ModelMapper
import com.example.domain.model.ControlItem
import com.example.domain.model.ControlStatus
import com.example.domain.status.ControlStatusCalculator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MaintenanceRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = MaintenanceRepository(database.maintenanceDao())
        viewModelScope.launch {
            repository.prepopulateIfEmpty(application)
        }
    }

    // Streams of data
    val items: StateFlow<List<MaintenanceItem>> = repository.allItems
        .map { list ->
            // Pass items through Domain Model Mapper and calculate status dynamically
            list.map { entity ->
                val domain = ModelMapper.toDomain(entity)
                // Map back to sync status from ControlStatusCalculator
                entity.copy(
                    isCompleted = domain.status == ControlStatus.OK && entity.isCompleted,
                    notes = when (domain.status) {
                        ControlStatus.OVERDUE -> "Atrasado! Verifique imediatamente"
                        ControlStatus.ATTENTION -> "Atenção necessária"
                        ControlStatus.OK -> entity.notes
                    }
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Direct domain model exposure for strict SDD alignment
    val controlItems: StateFlow<List<ControlItem>> = repository.allItems
        .map { list -> list.map { ModelMapper.toDomain(it) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val codes: StateFlow<List<AppCode>> = repository.allCodes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notes: StateFlow<List<AppNote>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val documents: StateFlow<List<DocumentItem>> = repository.allDocuments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val historyEntries: StateFlow<List<HistoryEntryEntity>> = repository.allHistoryEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search query for Archive tab
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Add item functions using Domain mapper or properties
    fun addMaintenanceItem(
        title: String,
        category: String,
        subtitle: String?,
        daysLeft: Int,
        notes: String?,
        smartReminder: Boolean,
        recurrence: String = "Nenhuma",
        alertDaysBefore: Int = 0
    ) {
        viewModelScope.launch {
            val entity = MaintenanceItem(
                title = title,
                category = category,
                subtitle = subtitle ?: category,
                daysLeft = daysLeft,
                isCompleted = false,
                notes = notes,
                smartReminder = smartReminder,
                recurrence = recurrence,
                alertDaysBefore = alertDaysBefore
            )
            // Verify and process via Domain mapping
            val domain = ModelMapper.toDomain(entity)
            val processedEntity = ModelMapper.toEntity(domain, isCompleted = false).copy(
                notes = notes, // Preserve custom notes
                smartReminder = smartReminder,
                recurrence = recurrence,
                alertDaysBefore = alertDaysBefore
            )
            val insertedId = repository.insertItem(processedEntity)
            val finalEntity = processedEntity.copy(id = insertedId.toInt())
            com.example.data.NotificationScheduler.scheduleNotification(getApplication(), finalEntity)
        }
    }

    fun updateMaintenanceItem(
        id: Int,
        title: String,
        category: String,
        subtitle: String?,
        daysLeft: Int,
        notes: String?,
        smartReminder: Boolean,
        recurrence: String = "Nenhuma",
        alertDaysBefore: Int = 0
    ) {
        viewModelScope.launch {
            val existing = repository.getItemById(id)
            if (existing != null) {
                val updated = existing.copy(
                    title = title,
                    category = category,
                    subtitle = subtitle ?: category,
                    daysLeft = daysLeft,
                    notes = notes,
                    smartReminder = smartReminder,
                    recurrence = recurrence,
                    alertDaysBefore = alertDaysBefore
                )
                repository.updateItem(updated)
                com.example.data.NotificationScheduler.scheduleNotification(getApplication(), updated)
            }
        }
    }

    fun deleteMaintenanceItem(id: Int) {
        viewModelScope.launch {
            val existing = repository.getItemById(id)
            if (existing != null) {
                repository.deleteItem(existing)
                com.example.data.NotificationScheduler.cancelNotification(getApplication(), id)
            }
        }
    }

    fun completeItem(itemId: Int) {
        completeControlItem(itemId)
    }

    fun completeControlItem(itemId: Int) {
        viewModelScope.launch {
            repository.completeControlItem(itemId, getApplication())
        }
    }

    fun restoreHistoryEntry(historyId: Int) {
        viewModelScope.launch {
            repository.restoreHistoryEntry(historyId)
        }
    }

    fun deleteHistoryEntry(historyId: Int) {
        viewModelScope.launch {
            repository.deleteHistoryEntryById(historyId)
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
