package com.example.data

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class RecurrenceHelperTest {

    private val baseDate = LocalDate.of(2026, 6, 20)

    @Test
    fun `test no recurrence returns identical daysLeft`() {
        val result = RecurrenceHelper.calculateDaysForNextRecurrence("Nenhuma", 10, baseDate)
        assertEquals(10, result)

        val resultInvalid = RecurrenceHelper.calculateDaysForNextRecurrence("Aleatorio", 5, baseDate)
        assertEquals(5, resultInvalid)
    }

    @Test
    fun `test monthly recurrence adds one month exactly`() {
        // BaseDate: 2026-06-20 + 0 daysLeft = 2026-06-20.
        // 2026-06-20 + 1 month = 2026-07-20.
        // Days between 2026-06-20 and 2026-07-20 is 30 days.
        val result = RecurrenceHelper.calculateDaysForNextRecurrence("Mensal", 0, baseDate)
        assertEquals(30, result)
    }

    @Test
    fun `test quarterly recurrence adds three months exactly`() {
        // BaseDate: 2026-06-20.
        // 2026-06-20 + 3 months = 2026-09-20.
        val result = RecurrenceHelper.calculateDaysForNextRecurrence("Trimestral", 0, baseDate)
        
        val expectedDate = baseDate.plusMonths(3)
        val expectedDays = java.time.temporal.ChronoUnit.DAYS.between(baseDate, expectedDate).toInt()
        assertEquals(expectedDays, result)
    }

    @Test
    fun `test semiannual recurrence adds six months exactly`() {
        // BaseDate: 2026-06-20 + 5 daysLeft = 2026-06-25.
        // 2026-06-25 + 6 months = 2026-12-25.
        // Days from baseDate (2026-06-20) to 2026-12-25.
        val result = RecurrenceHelper.calculateDaysForNextRecurrence("Semestral", 5, baseDate)
        
        val expectedDate = baseDate.plusDays(5).plusMonths(6)
        val expectedDays = java.time.temporal.ChronoUnit.DAYS.between(baseDate, expectedDate).toInt()
        assertEquals(expectedDays, result)
    }

    @Test
    fun `test annual recurrence adds one year exactly`() {
        val result = RecurrenceHelper.calculateDaysForNextRecurrence("Anual", 0, baseDate)
        
        val expectedDate = baseDate.plusYears(1)
        val expectedDays = java.time.temporal.ChronoUnit.DAYS.between(baseDate, expectedDate).toInt()
        assertEquals(expectedDays, result)
    }

    @Test
    fun `test end of month recurrence transitions safely without breaking`() {
        // 31st of January + 1 month -> should safely land on 28th of February.
        val endJan = LocalDate.of(2026, 1, 31)
        val result = RecurrenceHelper.calculateDaysForNextRecurrence("Mensal", 0, endJan)
        
        val expectedDate = endJan.plusMonths(1) // 2026-02-28
        val expectedDays = java.time.temporal.ChronoUnit.DAYS.between(endJan, expectedDate).toInt() // 28 days
        assertEquals(28, result)
        assertEquals(28, expectedDate.dayOfMonth)
    }

    @Test
    fun `test recurrence labels mapping`() {
        assertEquals("Nenhuma", RecurrenceHelper.getRecurrenceLabel("Nenhuma"))
        assertEquals("Mensal", RecurrenceHelper.getRecurrenceLabel("Mensal"))
        assertEquals("Trimestral", RecurrenceHelper.getRecurrenceLabel("Trimestral"))
        assertEquals("Semestral", RecurrenceHelper.getRecurrenceLabel("Semestral"))
        assertEquals("Anual", RecurrenceHelper.getRecurrenceLabel("Anual"))
        assertEquals("Personalizado", RecurrenceHelper.getRecurrenceLabel("Personalizado"))
    }

    @Test
    fun `test alert days labels mapping`() {
        assertEquals("No dia do vencimento", RecurrenceHelper.getAlertLabel(0))
        assertEquals("1 dia antes", RecurrenceHelper.getAlertLabel(1))
        assertEquals("3 dias antes", RecurrenceHelper.getAlertLabel(3))
        assertEquals("1 semana antes", RecurrenceHelper.getAlertLabel(7))
        assertEquals("1 mês antes", RecurrenceHelper.getAlertLabel(30))
        assertEquals("15 dias antes", RecurrenceHelper.getAlertLabel(15))
    }
}
