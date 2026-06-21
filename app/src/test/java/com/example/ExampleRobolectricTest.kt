package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppDatabase
import com.example.ui.MainViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Casa em Dia", appName)
  }

  @Test
  fun testMaintenanceItemRoomPersistence() = runBlocking {
    val application = ApplicationProvider.getApplicationContext<Application>()
    
    val testDb = androidx.room.Room.inMemoryDatabaseBuilder(application, AppDatabase::class.java)
        .allowMainThreadQueries()
        .build()
    AppDatabase.setTestInstance(testDb)

    val repository = com.example.data.MaintenanceRepository(testDb.maintenanceDao())

    repository.prepopulateIfEmpty(application)

    val items = repository.allItems.first()
    assert(items.isNotEmpty())

    val testItem = com.example.data.MaintenanceItem(
        title = "Teste de Persistencia real",
        category = "CASA",
        subtitle = "CASA",
        daysLeft = 5,
        isCompleted = false,
        notes = "Observacoes do item de teste"
    )
    repository.insertItem(testItem)

    val itemsAfterInsert = repository.allItems.first()
    val foundItem = itemsAfterInsert.find { it.title == "Teste de Persistencia real" }
    assertNotNull(foundItem)
    assertEquals("Observacoes do item de teste", foundItem?.notes)
    assertEquals(5, foundItem?.daysLeft)
    assertEquals("CASA", foundItem?.category)

    testDb.close()
    AppDatabase.setTestInstance(null)
  }

  @Test
  fun testCompletionAndHistoryPersistentFlow() = runBlocking {
    val application = ApplicationProvider.getApplicationContext<Application>()
    
    val testDb = androidx.room.Room.inMemoryDatabaseBuilder(application, AppDatabase::class.java)
        .allowMainThreadQueries()
        .build()
    AppDatabase.setTestInstance(testDb)

    val repository = com.example.data.MaintenanceRepository(testDb.maintenanceDao())

    repository.prepopulateIfEmpty(application)

    val itemToInsert = com.example.data.MaintenanceItem(
      title = "Teste IPVA",
      category = "CARRO",
      subtitle = "Carro",
      daysLeft = 14,
      isCompleted = false,
      notes = "Pagar IPVA deste ano"
    )
    repository.insertItem(itemToInsert)

    val activeItemsList = repository.allItems.first()
    val activeItem = activeItemsList.find { it.title == "Teste IPVA" }
    assertNotNull("Item active must be created and present", activeItem)
    val testItemId = activeItem!!.id

    assert(!activeItem.isCompleted)

    repository.completeControlItem(testItemId, application)

    val completedItemInDb = repository.getItemById(testItemId)
    assertNotNull(completedItemInDb)
    assert(completedItemInDb!!.isCompleted)
    assertEquals("Hoje", completedItemInDb.completedDateStr)

    val currentActiveItemsList = repository.allItems.first().filter { !it.isCompleted }
    val foundInActive = currentActiveItemsList.any { it.id == testItemId }
    assert(!foundInActive)

    val historyList = repository.allHistoryEntries.first()
    val foundInHistory = historyList.any { it.itemId == testItemId }
    assert(foundInHistory)

    val historyEntry = historyList.find { it.itemId == testItemId }
    assertNotNull(historyEntry)
    assertEquals("Teste IPVA", historyEntry?.title)
    assertEquals("CARRO", historyEntry?.category)
    assertEquals("Carro", historyEntry?.subtitle)
    assertEquals("Hoje", historyEntry?.completedDateStr)

    testDb.close()
    AppDatabase.setTestInstance(null)
  }
}
