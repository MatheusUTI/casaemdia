package com.example.domain.status

import com.example.domain.model.ControlStatus
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object ControlStatusCalculator {
    fun calculateStatus(
        limitDate: LocalDate?,
        alertWindowDays: Int,
        predictedMileage: Int?,
        alertWindowMileage: Int?,
        currentMileage: Int?,
        currentDate: LocalDate = LocalDate.now()
    ): ControlStatus {
        // 1. Mileage check (if applicable)
        if (predictedMileage != null && currentMileage != null) {
            if (currentMileage >= predictedMileage) {
                return ControlStatus.OVERDUE
            }
            if (alertWindowMileage != null) {
                val remMileage = predictedMileage - currentMileage
                if (remMileage <= alertWindowMileage) {
                    return ControlStatus.ATTENTION
                }
            }
        }

        // 2. Date check (if applicable)
        if (limitDate != null) {
            if (limitDate.isBefore(currentDate)) {
                return ControlStatus.OVERDUE
            }
            if (limitDate == currentDate) {
                return ControlStatus.ATTENTION
            }
            val daysBetween = ChronoUnit.DAYS.between(currentDate, limitDate)
            if (daysBetween >= 0 && daysBetween <= alertWindowDays) {
                return ControlStatus.ATTENTION
            }
        }

        return ControlStatus.OK
    }
}
