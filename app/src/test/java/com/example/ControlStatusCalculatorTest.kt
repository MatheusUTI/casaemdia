package com.example

import com.example.domain.model.*
import com.example.domain.status.ControlStatusCalculator
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class ControlStatusCalculatorTest {

    private val baseDate = LocalDate.of(2026, 6, 18)

    @Test
    fun `test item with past limit date returns OVERDUE`() {
        val limitDate = baseDate.minusDays(1)
        val status = ControlStatusCalculator.calculateStatus(
            limitDate = limitDate,
            alertWindowDays = 7,
            predictedMileage = null,
            alertWindowMileage = null,
            currentMileage = null,
            currentDate = baseDate
        )
        assertEquals(ControlStatus.OVERDUE, status)
    }

    @Test
    fun `test item with limit date today returns ATTENTION`() {
        val limitDate = baseDate
        val status = ControlStatusCalculator.calculateStatus(
            limitDate = limitDate,
            alertWindowDays = 7,
            predictedMileage = null,
            alertWindowMileage = null,
            currentMileage = null,
            currentDate = baseDate
        )
        assertEquals(ControlStatus.ATTENTION, status)
    }

    @Test
    fun `test item within alert window days returns ATTENTION`() {
        val limitDate = baseDate.plusDays(5) // within 7 days window
        val status = ControlStatusCalculator.calculateStatus(
            limitDate = limitDate,
            alertWindowDays = 7,
            predictedMileage = null,
            alertWindowMileage = null,
            currentMileage = null,
            currentDate = baseDate
        )
        assertEquals(ControlStatus.ATTENTION, status)
    }

    @Test
    fun `test item outside alert window days returns OK`() {
        val limitDate = baseDate.plusDays(10) // outside 7 days window
        val status = ControlStatusCalculator.calculateStatus(
            limitDate = limitDate,
            alertWindowDays = 7,
            predictedMileage = null,
            alertWindowMileage = null,
            currentMileage = null,
            currentDate = baseDate
        )
        assertEquals(ControlStatus.OK, status)
    }

    @Test
    fun `test item with mileage exceeded returns OVERDUE`() {
        val status = ControlStatusCalculator.calculateStatus(
            limitDate = null,
            alertWindowDays = 7,
            predictedMileage = 45000,
            alertWindowMileage = 1000,
            currentMileage = 45000, // exact match
            currentDate = baseDate
        )
        assertEquals(ControlStatus.OVERDUE, status)

        val status2 = ControlStatusCalculator.calculateStatus(
            limitDate = null,
            alertWindowDays = 7,
            predictedMileage = 45000,
            alertWindowMileage = 1000,
            currentMileage = 45100, // over limit
            currentDate = baseDate
        )
        assertEquals(ControlStatus.OVERDUE, status2)
    }

    @Test
    fun `test item within alert window mileage returns ATTENTION`() {
        val status = ControlStatusCalculator.calculateStatus(
            limitDate = null,
            alertWindowDays = 7,
            predictedMileage = 50000,
            alertWindowMileage = 1000,
            currentMileage = 49500, // 500 km left, <= 1000 threshold
            currentDate = baseDate
        )
        assertEquals(ControlStatus.ATTENTION, status)
    }

    @Test
    fun `test item outside alert window mileage returns OK`() {
        val status = ControlStatusCalculator.calculateStatus(
            limitDate = null,
            alertWindowDays = 7,
            predictedMileage = 50000,
            alertWindowMileage = 1000,
            currentMileage = 48000, // 2000 km left, > 1000 threshold
            currentDate = baseDate
        )
        assertEquals(ControlStatus.OK, status)
    }

    @Test
    fun `test asset creation`() {
        val asset = Asset(
            id = "1",
            name = "Palio 1.6",
            type = AssetType.CAR,
            description = "Uso Diário",
            identifier = "BRA2E19"
        )
        assertEquals("1", asset.id)
        assertEquals("Palio 1.6", asset.name)
        assertEquals(AssetType.CAR, asset.type)
        assertEquals("BRA2E19", asset.identifier)
    }

    @Test
    fun `test control item creation`() {
        val item = ControlItem(
            id = "item-101",
            assetId = "asset-1",
            title = "Troca de óleo",
            type = ControlItemType.MILEAGE,
            status = ControlStatus.ATTENTION,
            limitDate = LocalDate.of(2026, 7, 1),
            predictedMileage = 50000,
            currentMileage = 49500,
            notes = "Restam apenas 500 km"
        )
        assertEquals("item-101", item.id)
        assertEquals("asset-1", item.assetId)
        assertEquals("Troca de óleo", item.title)
        assertEquals(ControlItemType.MILEAGE, item.type)
        assertEquals(ControlStatus.ATTENTION, item.status)
        assertEquals(50000, item.predictedMileage)
        assertEquals(49500, item.currentMileage)
    }

    @Test
    fun `test history entry registration`() {
        val history = HistoryEntry(
            id = "hist-1",
            itemId = "item-101",
            date = LocalDate.of(2026, 6, 10),
            title = "Histórico de Troca de óleo",
            cost = 250.00,
            notes = "Serviço OK"
        )
        assertEquals("hist-1", history.id)
        assertEquals("item-101", history.itemId)
        assertEquals(LocalDate.of(2026, 6, 10), history.date)
        assertEquals("Histórico de Troca de óleo", history.title)
        assertEquals(250.00, history.cost)
        assertEquals("Serviço OK", history.notes)
    }
}
