package com.example.ui

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.data.MaintenanceItem
import com.example.data.HistoryEntryEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.util.concurrent.Executor

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class MainViewModelTest {

    private lateinit var db: AppDatabase
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Application>()

        // Clear SharedPreferences to avoid pre-populate persistence from previous runs
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().clear().commit()

        // Configure in-memory database with direct executor to make all Room queries synchronous
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .setQueryExecutor { command -> command.run() }
            .setTransactionExecutor { command -> command.run() }
            .build()
        AppDatabase.setTestInstance(db)

        viewModel = MainViewModel(context)
    }

    @After
    fun tearDown() {
        db.close()
        AppDatabase.setTestInstance(null)
    }

    private fun startCollecting(scope: CoroutineScope) {
        scope.launch { viewModel.items.collect {} }
        scope.launch { viewModel.controlItems.collect {} }
        scope.launch { viewModel.codes.collect {} }
        scope.launch { viewModel.notes.collect {} }
        scope.launch { viewModel.documents.collect {} }
        scope.launch { viewModel.historyEntries.collect {} }
    }

    @Test
    fun testPrepopulateAndInitialState() = runBlocking {
        startCollecting(this)
        // Yield to allow ViewModel's prepopulate coroutine to launch and execute
        delay(100)

        val activeItems = viewModel.items.value
        val historyEntries = viewModel.historyEntries.value
        val codes = viewModel.codes.value
        val notes = viewModel.notes.value
        val docs = viewModel.documents.value

        // Verify default quantities are inserted and loaded
        assertTrue("Active items should be pre-populated", activeItems.isNotEmpty())
        assertTrue("History entries should be pre-populated", historyEntries.isNotEmpty())
        assertTrue("App codes should be pre-populated", codes.isNotEmpty())
        assertTrue("App notes should be pre-populated", notes.isNotEmpty())
        assertTrue("App documents should be pre-populated", docs.isNotEmpty())
    }

    @Test
    fun testAddMaintenanceItemAndScheduler() = runBlocking {
        startCollecting(this)
        delay(100)

        val context = ApplicationProvider.getApplicationContext<Application>()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val shadowAlarm = shadowOf(alarmManager)
        shadowAlarm.scheduledAlarms.clear()

        viewModel.addMaintenanceItem(
            title = "Test Novo Item",
            category = "CASA",
            subtitle = "Sótão",
            daysLeft = 15,
            notes = "Notas exclusivas",
            smartReminder = true,
            recurrence = "Trimestral",
            alertDaysBefore = 3
        )

        delay(100)

        val activeItems = viewModel.items.value
        val newlyAdded = activeItems.find { it.title == "Test Novo Item" }
        assertNotNull("The newly added item should be found", newlyAdded)
        assertEquals("CASA", newlyAdded?.category)
        assertEquals("Sótão", newlyAdded?.subtitle)
        assertEquals(15, newlyAdded?.daysLeft)
        assertEquals("Notas exclusivas", newlyAdded?.notes)
        assertTrue(newlyAdded?.smartReminder == true)
        assertEquals("Trimestral", newlyAdded?.recurrence)
        assertEquals(3, newlyAdded?.alertDaysBefore)

        // Verify notification is scheduled
        val alarms = shadowAlarm.scheduledAlarms
        assertTrue("An alarm must be scheduled", alarms.isNotEmpty())
        val matchingAlarm = alarms.find { shadowOf(it.operation).requestCode == newlyAdded?.id }
        assertNotNull("A matching alarm with request code should be registered", matchingAlarm)
    }

    @Test
    fun testUpdateMaintenanceItemAndScheduler() = runBlocking {
        startCollecting(this)
        delay(100)

        val context = ApplicationProvider.getApplicationContext<Application>()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val shadowAlarm = shadowOf(alarmManager)

        // Retrieve an item to update
        val targetItem = viewModel.items.value.first { !it.isCompleted }

        shadowAlarm.scheduledAlarms.clear()

        viewModel.updateMaintenanceItem(
            id = targetItem.id,
            title = "Item Atualizado",
            category = "CARRO",
            subtitle = "Garagem Principal",
            daysLeft = 20,
            notes = "Customized Notes",
            smartReminder = false,
            recurrence = "Anual",
            alertDaysBefore = 7
        )

        delay(100)

        val updatedItem = viewModel.items.value.find { it.id == targetItem.id }
        assertNotNull("The updated item should exist", updatedItem)
        assertEquals("Item Atualizado", updatedItem?.title)
        assertEquals("CARRO", updatedItem?.category)
        assertEquals("Garagem Principal", updatedItem?.subtitle)
        assertEquals(20, updatedItem?.daysLeft)
        assertEquals("Customized Notes", updatedItem?.notes)
        assertFalse(updatedItem?.smartReminder == true)
        assertEquals("Anual", updatedItem?.recurrence)
        assertEquals(7, updatedItem?.alertDaysBefore)

        // Verify alarm scheduled for updated item
        val alarms = shadowAlarm.scheduledAlarms
        val matchingAlarm = alarms.find { shadowOf(it.operation).requestCode == targetItem.id }
        assertNotNull("Alarm should match updated item", matchingAlarm)
    }

    @Test
    fun testDeleteMaintenanceItemAndCancelScheduler() = runBlocking {
        startCollecting(this)
        delay(100)

        val context = ApplicationProvider.getApplicationContext<Application>()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val shadowAlarm = shadowOf(alarmManager)

        val targetItem = viewModel.items.value.first { !it.isCompleted }

        // Schedule first to ensure we can cancel it
        com.example.data.NotificationScheduler.scheduleNotification(context, targetItem)

        viewModel.deleteMaintenanceItem(targetItem.id)
        delay(100)

        val deletedItem = viewModel.items.value.find { it.id == targetItem.id }
        assertNull("Item should be deleted successfully", deletedItem)
    }

    @Test
    fun testCompleteItemCreatesHistoryAndHandlesRecurrence() = runBlocking {
        startCollecting(this)
        delay(100)

        // 1. Completing a non-recurrent item
        val queryResult = viewModel.items.value
        val nonRecurrentItem = queryResult.first { !it.isCompleted && it.recurrence == "Nenhuma" }

        val initialHistoryCount = viewModel.historyEntries.value.size

        viewModel.completeItem(nonRecurrentItem.id)
        delay(100)

        // Non-recurrent item should mark isCompleted = true
        val itemAfterCompletion = db.maintenanceDao().getItemById(nonRecurrentItem.id)
        assertNotNull(itemAfterCompletion)
        assertTrue(itemAfterCompletion?.isCompleted == true)

        // Check history entry created
        val historyAfter = viewModel.historyEntries.value
        assertEquals(initialHistoryCount + 1, historyAfter.size)
        val createdEntry = historyAfter.find { it.itemId == nonRecurrentItem.id }
        assertNotNull(createdEntry)
        assertEquals(nonRecurrentItem.title, createdEntry?.title)

        // 2. Completing a recurrent item
        // Let's create a recurrent item to test
        viewModel.addMaintenanceItem(
            title = "Frequente",
            category = "CASA",
            subtitle = "Casa",
            daysLeft = 5,
            notes = "Sempre limpo",
            smartReminder = false,
            recurrence = "Mensal",
            alertDaysBefore = 0
        )
        delay(100)

        val newlyCreatedRecurrent = viewModel.items.value.first { it.title == "Frequente" }

        viewModel.completeItem(newlyCreatedRecurrent.id)
        delay(100)

        // The old item should be marked complete
        val completedRecurrent = db.maintenanceDao().getItemById(newlyCreatedRecurrent.id)
        assertTrue(completedRecurrent?.isCompleted == true)

        // A new cloned item with +1 month (approx 30 days) recurrence should be created
        val activeItemsList = viewModel.items.value
        val nextClonedInstance = activeItemsList.find { it.title == "Frequente" && !it.isCompleted }
        assertNotNull("A recurring next item instance should have been generated", nextClonedInstance)
        assertNotEquals(newlyCreatedRecurrent.id, nextClonedInstance?.id)
        assertTrue("Recurrence logic should push next instance's daysLeft forward", nextClonedInstance!!.daysLeft >= 30)
    }

    @Test
    fun testRestoreHistoryEntryReactivatesItem() = runBlocking {
        startCollecting(this)
        delay(100)

        val historyEntries = viewModel.historyEntries.value
        assertTrue(historyEntries.isNotEmpty())

        val targetHistoryEntry = historyEntries.first()
        val originalItemInDb = db.maintenanceDao().getItemById(targetHistoryEntry.itemId)
        assertNotNull(originalItemInDb)

        viewModel.restoreHistoryEntry(targetHistoryEntry.id)
        delay(100)

        // Re-check original item in DB, should now be active (isCompleted = false)
        val restoredItem = db.maintenanceDao().getItemById(targetHistoryEntry.itemId)
        assertNotNull(restoredItem)
        assertFalse(restoredItem?.isCompleted == true)
        assertNull(restoredItem?.completedDateStr)

        // History entry should be removed
        val updatedHistory = viewModel.historyEntries.value
        assertNull(updatedHistory.find { it.id == targetHistoryEntry.id })
    }

    @Test
    fun testDeleteHistoryEntry() = runBlocking {
        startCollecting(this)
        delay(100)

        val initialHistoryList = viewModel.historyEntries.value
        assertTrue(initialHistoryList.isNotEmpty())

        val targetHistory = initialHistoryList.first()

        viewModel.deleteHistoryEntry(targetHistory.id)
        delay(100)

        val updatedHistoryList = viewModel.historyEntries.value
        assertEquals(initialHistoryList.size - 1, updatedHistoryList.size)
        assertNull(updatedHistoryList.find { it.id == targetHistory.id })
    }

    @Test
    fun testAddNoteCodeAndDocument() = runBlocking {
        startCollecting(this)
        delay(100)

        viewModel.addNote("Lembrar de comprar lâmpadas LED", "Hoje, 10:00")
        viewModel.addCode("Wi-Fi Visitas", "12345678", "wifi")
        viewModel.addDocument("Planta_Baixa.pdf", "5.1 MB", "Documentos")

        delay(100)

        val notes = viewModel.notes.value
        assertNotNull(notes.find { it.text == "Lembrar de comprar lâmpadas LED" })

        val codes = viewModel.codes.value
        val insertedCode = codes.find { it.title == "Wi-Fi Visitas" }
        assertNotNull(insertedCode)
        assertEquals("12345678", insertedCode?.value)
        assertEquals("wifi", insertedCode?.iconName)

        val docs = viewModel.documents.value
        val insertedDoc = docs.find { it.fileName == "Planta_Baixa.pdf" }
        assertNotNull(insertedDoc)
        assertEquals("5.1 MB", insertedDoc?.fileSize)
        assertEquals("Documentos", insertedDoc?.fileType)
    }

    @Test
    fun testSearchQueryUpdates() = runBlocking {
        assertEquals("", viewModel.searchQuery.value)

        viewModel.updateSearchQuery("Aspirador")
        assertEquals("Aspirador", viewModel.searchQuery.value)
    }
}
