package com.example.data

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object RecurrenceHelper {
    fun calculateDaysForNextRecurrence(recurrence: String, currentDaysLeft: Int): Int {
        return calculateDaysForNextRecurrence(recurrence, currentDaysLeft, LocalDate.now())
    }

    fun calculateDaysForNextRecurrence(recurrence: String, currentDaysLeft: Int, referenceDate: LocalDate): Int {
        val targetDate = referenceDate.plusDays(currentDaysLeft.toLong())
        val nextDate = when (recurrence) {
            "Mensal" -> targetDate.plusMonths(1)
            "Trimestral" -> targetDate.plusMonths(3)
            "Semestral" -> targetDate.plusMonths(6)
            "Anual" -> targetDate.plusYears(1)
            else -> targetDate
        }
        return ChronoUnit.DAYS.between(referenceDate, nextDate).toInt()
    }

    fun getRecurrenceLabel(recurrence: String): String {
        return when (recurrence) {
            "Nenhuma" -> "Nenhuma"
            "Mensal" -> "Mensal"
            "Trimestral" -> "Trimestral"
            "Semestral" -> "Semestral"
            "Anual" -> "Anual"
            else -> recurrence
        }
    }

    fun getAlertLabel(daysBefore: Int): String {
        return when (daysBefore) {
            0 -> "No dia do vencimento"
            1 -> "1 dia antes"
            3 -> "3 dias antes"
            7 -> "1 semana antes"
            30 -> "1 mês antes"
            else -> "$daysBefore dias antes"
        }
    }
}
