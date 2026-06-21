package com.example.data.mapper

import com.example.data.MaintenanceItem
import com.example.domain.model.ControlItem
import com.example.domain.model.ControlItemType
import com.example.domain.model.ControlStatus
import com.example.domain.status.ControlStatusCalculator
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object ModelMapper {
    /**
     * Map clean Room MaintenanceItem to Domain ControlItem
     */
    fun toDomain(item: MaintenanceItem): ControlItem {
        val assetId = when (item.category) {
            "CARRO" -> "1"
            "CASA" -> "2"
            else -> "3"
        }
        
        val type = when {
            item.title.contains("óleo", ignoreCase = true) || 
            item.title.contains("Filtro", ignoreCase = true) ||
            item.title.contains("Ar Cond", ignoreCase = true) -> ControlItemType.MILEAGE
            
            item.title.contains("Seguro", ignoreCase = true) || 
            item.title.contains("IPTU", ignoreCase = true) -> ControlItemType.FIXED_DATE
            
            else -> ControlItemType.TIME_INTERVAL
        }

        val limitDate = LocalDate.now().plusDays(item.daysLeft.toLong())
        val alertWindowDays = 7
        
        // Approximate mileage fields if it's a mileage type
        val predictedMileage = if (type == ControlItemType.MILEAGE) 10000 else null
        val alertWindowMileage = if (type == ControlItemType.MILEAGE) 1000 else null
        val currentMileage = if (type == ControlItemType.MILEAGE) {
            val offset = if (item.daysLeft < 0) 500 else -500
            10000 + offset
        } else null

        val calculatedStatus = if (item.isCompleted) {
            ControlStatus.OK
        } else {
            ControlStatusCalculator.calculateStatus(
                limitDate = limitDate,
                alertWindowDays = alertWindowDays,
                predictedMileage = predictedMileage,
                alertWindowMileage = alertWindowMileage,
                currentMileage = currentMileage,
                currentDate = LocalDate.now()
            )
        }

        return ControlItem(
            id = item.id.toString(),
            assetId = assetId,
            title = item.title,
            type = type,
            status = calculatedStatus,
            limitDate = limitDate,
            alertWindowDays = alertWindowDays,
            predictedMileage = predictedMileage,
            alertWindowMileage = alertWindowMileage,
            currentMileage = currentMileage,
            notes = item.notes
        )
    }

    /**
     * Map Domain ControlItem back to Room MaintenanceItem
     */
    fun toEntity(domain: ControlItem, isCompleted: Boolean = false): MaintenanceItem {
        val category = when (domain.assetId) {
            "1" -> "CARRO"
            "2" -> "CASA"
            else -> "OUTRO"
        }
        
        val daysLeft = if (domain.limitDate != null) {
            ChronoUnit.DAYS.between(LocalDate.now(), domain.limitDate).toInt()
        } else {
            0
        }

        return MaintenanceItem(
            id = domain.id.toIntOrNull() ?: 0,
            title = domain.title,
            category = category,
            subtitle = if (category == "CARRO") "CARRO" else "CASA",
            daysLeft = daysLeft,
            isCompleted = isCompleted,
            completedDateStr = if (isCompleted) "Hoje" else null,
            cost = null,
            notes = domain.notes,
            detailValue = null,
            smartReminder = false
        )
    }
}
