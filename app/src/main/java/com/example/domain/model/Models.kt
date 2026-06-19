package com.example.domain.model

import java.time.LocalDate

enum class AssetType {
    CAR, HOME, DOCUMENT, OTHER
}

enum class ControlItemType {
    FIXED_DATE, TIME_INTERVAL, MILEAGE, INFO, DOCUMENT
}

enum class ControlStatus {
    OK, ATTENTION, OVERDUE
}

data class Asset(
    val id: String,
    val name: String,
    val type: AssetType,
    val description: String? = null,
    val identifier: String? = null
)

data class ControlItem(
    val id: String,
    val assetId: String,
    val title: String,
    val type: ControlItemType,
    val status: ControlStatus,
    val limitDate: LocalDate? = null,
    val alertWindowDays: Int = 7,
    val predictedMileage: Int? = null,
    val alertWindowMileage: Int? = null,
    val currentMileage: Int? = null,
    val notes: String? = null
)

data class Attachment(
    val id: String,
    val itemId: String,
    val name: String,
    val type: String,
    val size: String,
    val uri: String
)

data class HistoryEntry(
    val id: String,
    val itemId: String,
    val date: LocalDate,
    val title: String,
    val cost: Double? = null,
    val notes: String? = null
)
