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

  @Test
  fun testBackupRestoreRobustnessAndTransactions() = runBlocking {
    val application = ApplicationProvider.getApplicationContext<Application>()
    
    val testDb = androidx.room.Room.inMemoryDatabaseBuilder(application, AppDatabase::class.java)
        .allowMainThreadQueries()
        .build()
    AppDatabase.setTestInstance(testDb)

    val repository = com.example.data.MaintenanceRepository(testDb.maintenanceDao())

    // 1. Initial State: Pre-populated items exist
    repository.prepopulateIfEmpty(application)
    val initialItemsCount = repository.allItems.first().size
    assert(initialItemsCount > 0)

    // 2. Scenario A: Corrupted / Malformed JSON string import
    val corruptJson = "{ \"maintenanceItems\": [ {title: \"Semi JSON\" " // missing end brackets, quotes, etc.
    var corruptResultException: Exception? = null
    try {
        com.example.data.BackupHelper.restoreFromJsonString(repository, corruptJson)
    } catch (e: Exception) {
        corruptResultException = e
    }
    
    assertNotNull("Should throw an exception for corrupted JSON", corruptResultException)
    assert(corruptResultException is IllegalArgumentException)
    assertEquals("O arquivo de backup está corrompido ou possui formato JSON inválido.", corruptResultException?.message)
    // Check database has NOT been cleared and still has original count
    assertEquals(initialItemsCount, repository.allItems.first().size)

    // 3. Scenario B: Incompatible structure / missing required properties JSON string import
    // Note: Since moshi is strict about property fields, malformed structure yields JsonDataException
    val incompatibleJson = "{ \"maintenanceItems\": [ { \"id\": \"not-an-int\", \"title\": 123 } ] }"
    var schemaException: Exception? = null
    try {
        com.example.data.BackupHelper.restoreFromJsonString(repository, incompatibleJson)
    } catch (e: Exception) {
        schemaException = e
    }
    
    assertNotNull("Should throw an exception for incompatible schema", schemaException)
    assert(schemaException is IllegalArgumentException)
    assertEquals("Os dados de backup possuem uma estrutura ou esquema incompatível.", schemaException?.message)
    // Check database has NOT been cleared and still has original count
    assertEquals(initialItemsCount, repository.allItems.first().size)

    // 4. Scenario C: SQLite Transaction failure rollback
    // Create an item list that fails iteration halfway through inserting
    val validItem = com.example.data.MaintenanceItem(
        title = "Item Transacional",
        category = "CASA",
        subtitle = "CASA",
        daysLeft = 1
    )
    val maliciousItems = object : ArrayList<com.example.data.MaintenanceItem>() {
        override fun iterator(): MutableIterator<com.example.data.MaintenanceItem> {
            val parent = super.iterator()
            return object : MutableIterator<com.example.data.MaintenanceItem> {
                var count = 0
                override fun hasNext() = parent.hasNext()
                override fun next(): com.example.data.MaintenanceItem {
                    if (count++ > 0) {
                        throw RuntimeException("Falha simulada no meio da transação!")
                    }
                    return parent.next()
                }
                override fun remove() = parent.remove()
            }
        }
    }
    maliciousItems.add(validItem)
    maliciousItems.add(validItem) // The second one will throw when requested

    var transactionException: Exception? = null
    try {
        repository.restoreDatabaseTransaction(
            items = maliciousItems,
            codes = emptyList(),
            notes = emptyList(),
            docs = emptyList(),
            history = emptyList()
        )
    } catch (e: Exception) {
        transactionException = e
    }

    assertNotNull("Should throw an exception due to iterator runtime exception during transaction", transactionException)
    // Verify rollback! If rolled back, the "Item Transacional" inserted first is completely removed,
    // and the original elements are restored!
    val currentItems = repository.allItems.first()
    assertEquals("Database must successfully ROLLBACK to its exact initial state on failure", initialItemsCount, currentItems.size)
    assert(!currentItems.any { it.title == "Item Transacional" })

    testDb.close()
    AppDatabase.setTestInstance(null)
  }

  @Test
  fun testBackupRestoreViaStreamsAndSafInconsistencies() = runBlocking {
    val application = ApplicationProvider.getApplicationContext<Application>()
    
    val testDb = androidx.room.Room.inMemoryDatabaseBuilder(application, AppDatabase::class.java)
        .allowMainThreadQueries()
        .build()
    AppDatabase.setTestInstance(testDb)

    val repository = com.example.data.MaintenanceRepository(testDb.maintenanceDao())

    // 1. Pre-populate and get initial count
    repository.prepopulateIfEmpty(application)
    val initialItemsCount = repository.allItems.first().size
    assert(initialItemsCount > 0)

    // 2. Scenario D: Successful backup write to stream and subsequent restore
    val outputStream = java.io.ByteArrayOutputStream()
    val exportSuccess = com.example.data.BackupHelper.writeBackupToStream(repository, outputStream)
    assert(exportSuccess)
    val exportString = outputStream.toString("UTF-8")
    assert(exportString.contains("maintenanceItems"))

    // Insert a custom item to check post-restore state
    val customItem = com.example.data.MaintenanceItem(
        title = "Item Temporario",
        category = "CASA",
        subtitle = "CASA",
        daysLeft = 2
    )
    repository.insertItem(customItem)
    assertEquals(initialItemsCount + 1, repository.allItems.first().size)

    // Restore back the export stream (which doesn't have "Item Temporario")
    val inputStream = java.io.ByteArrayInputStream(exportString.toByteArray(Charsets.UTF_8))
    val restoreSuccess = com.example.data.BackupHelper.restoreFromStream(repository, inputStream)
    assert(restoreSuccess)

    // Confirm that "Item Temporario" was deleted and we returned to original count
    val restoredItems = repository.allItems.first()
    assertEquals(initialItemsCount, restoredItems.size)
    assert(!restoredItems.any { it.title == "Item Temporario" })

    // 3. Scenario E: Empty stream import (causes IllegalStateException/IOException)
    val emptyStream = java.io.ByteArrayInputStream(ByteArray(0))
    var emptyException: Exception? = null
    try {
        com.example.data.BackupHelper.restoreFromStream(repository, emptyStream)
    } catch (e: Exception) {
        emptyException = e
    }
    assertNotNull("Should throw exception for empty stream", emptyException)
    // The message generated when json is blank
    assertEquals("O arquivo de backup está vazio.", emptyException?.message)
    assertEquals(initialItemsCount, repository.allItems.first().size)

    // 4. Scenario F: Corrupt/Malformed stream import
    val corruptStream = java.io.ByteArrayInputStream("{ corrupt_json: ".toByteArray(Charsets.UTF_8))
    var corruptException: Exception? = null
    try {
        com.example.data.BackupHelper.restoreFromStream(repository, corruptStream)
    } catch (e: Exception) {
        corruptException = e
    }
    assertNotNull("Should throw exception for corrupt SAF stream", corruptException)
    assertEquals("O arquivo de backup está corrompido ou possui formato JSON inválido.", corruptException?.message)
    assertEquals(initialItemsCount, repository.allItems.first().size)

    testDb.close()
    AppDatabase.setTestInstance(null)
  }
}
