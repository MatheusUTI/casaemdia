package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Entity(tableName = "maintenance_items")
data class MaintenanceItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "CARRO", "CASA", "OUTRO"
    val subtitle: String? = null, // e.g. "Carro" or "Casa da Serra" or "Residência Principal"
    val daysLeft: Int = 0, // days difference: negative is overdue, 0 is today, positive is future
    val isCompleted: Boolean = false,
    val completedDateStr: String? = null, // e.g. "12 Outubro 2023"
    val cost: Double? = null,
    val notes: String? = null,
    val detailValue: String? = null, // optional
    val smartReminder: Boolean = false,
    val recurrence: String = "Nenhuma",
    val alertDaysBefore: Int = 0
)

@Entity(tableName = "app_codes")
data class AppCode(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String, // e.g. "Wi-Fi Casa"
    val value: String, // e.g. "Su3nh@F0rt3!"
    val iconName: String // "wifi" or "paint"
)

@Entity(tableName = "app_notes")
data class AppNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val dateStr: String // e.g. "Hoje, 09:41"
)

@Entity(tableName = "document_items")
data class DocumentItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fileName: String,
    val fileSize: String, // "2.4 MB"
    val fileType: String // "Eletrodomésticos" or "Recibos"
)

@Entity(tableName = "history_entries")
data class HistoryEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemId: Int,
    val title: String,
    val category: String, // "CARRO", "CASA"
    val subtitle: String? = null,
    val completedDateStr: String? = null, // e.g. "12 Outubro 2023" or "Hoje"
    val cost: Double? = null,
    val notes: String? = null
)

@Dao
interface MaintenanceDao {
    @Query("SELECT * FROM maintenance_items ORDER BY isCompleted ASC, daysLeft ASC")
    fun getAllItems(): Flow<List<MaintenanceItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: MaintenanceItem): Long

    @Update
    suspend fun updateItem(item: MaintenanceItem)

    @Delete
    suspend fun deleteItem(item: MaintenanceItem)

    @Query("SELECT * FROM maintenance_items WHERE id = :id")
    suspend fun getItemById(id: Int): MaintenanceItem?

    // AppCode queries
    @Query("SELECT * FROM app_codes ORDER BY id DESC")
    fun getAllCodes(): Flow<List<AppCode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCode(code: AppCode)

    // AppNote queries
    @Query("SELECT * FROM app_notes ORDER BY id DESC")
    fun getAllNotes(): Flow<List<AppNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: AppNote)

    // Document queries
    @Query("SELECT * FROM document_items ORDER BY id DESC")
    fun getAllDocuments(): Flow<List<DocumentItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(doc: DocumentItem)

    // HistoryEntryEntity queries
    @Query("SELECT * FROM history_entries ORDER BY id DESC")
    fun getAllHistoryEntries(): Flow<List<HistoryEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryEntry(entry: HistoryEntryEntity)

    @Query("SELECT * FROM history_entries WHERE id = :id")
    suspend fun getHistoryEntryById(id: Int): HistoryEntryEntity?

    @Delete
    suspend fun deleteHistoryEntry(entry: HistoryEntryEntity)
}

@Database(
    entities = [
        MaintenanceItem::class,
        AppCode::class,
        AppNote::class,
        DocumentItem::class,
        HistoryEntryEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun maintenanceDao(): MaintenanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun setTestInstance(db: AppDatabase?) {
            INSTANCE = db
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "casa_em_dia_database"
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class MaintenanceRepository(private val dao: MaintenanceDao) {
    val allItems: Flow<List<MaintenanceItem>> = dao.getAllItems()
    val allCodes: Flow<List<AppCode>> = dao.getAllCodes()
    val allNotes: Flow<List<AppNote>> = dao.getAllNotes()
    val allDocuments: Flow<List<DocumentItem>> = dao.getAllDocuments()
    val allHistoryEntries: Flow<List<HistoryEntryEntity>> = dao.getAllHistoryEntries()

    suspend fun insertItem(item: MaintenanceItem): Long = dao.insertItem(item)
    suspend fun updateItem(item: MaintenanceItem) = dao.updateItem(item)
    suspend fun deleteItem(item: MaintenanceItem) = dao.deleteItem(item)
    suspend fun getItemById(id: Int): MaintenanceItem? = dao.getItemById(id)

    suspend fun insertCode(code: AppCode) = dao.insertCode(code)
    suspend fun insertNote(note: AppNote) = dao.insertNote(note)
    suspend fun insertDocument(doc: DocumentItem) = dao.insertDocument(doc)
    suspend fun insertHistoryEntry(entry: HistoryEntryEntity) = dao.insertHistoryEntry(entry)
    suspend fun getHistoryEntryById(id: Int): HistoryEntryEntity? = dao.getHistoryEntryById(id)
    suspend fun deleteHistoryEntry(entry: HistoryEntryEntity) = dao.deleteHistoryEntry(entry)

    suspend fun restoreHistoryEntry(historyId: Int) {
        val entry = dao.getHistoryEntryById(historyId)
        if (entry != null) {
            val item = dao.getItemById(entry.itemId)
            if (item != null) {
                val updated = item.copy(
                    isCompleted = false,
                    completedDateStr = null
                )
                dao.updateItem(updated)
            }
            dao.deleteHistoryEntry(entry)
        }
    }

    suspend fun deleteHistoryEntryById(historyId: Int) {
        val entry = dao.getHistoryEntryById(historyId)
        if (entry != null) {
            dao.deleteHistoryEntry(entry)
        }
    }

    suspend fun completeControlItem(itemId: Int, context: android.content.Context) {
        val item = dao.getItemById(itemId)
        if (item != null) {
            val updated = item.copy(
                isCompleted = true,
                completedDateStr = "Hoje"
            )
            dao.updateItem(updated)
            
            val entry = HistoryEntryEntity(
                itemId = item.id,
                title = item.title,
                category = item.category,
                subtitle = item.subtitle ?: item.category,
                completedDateStr = "Hoje",
                cost = item.cost ?: 0.0,
                notes = item.notes ?: "Concluido com sucesso"
            )
            dao.insertHistoryEntry(entry)

            // Cancel notification for completed item
            NotificationScheduler.cancelNotification(context, item.id)

            // If recurrent, automatically create the next active item!
            if (item.recurrence != "Nenhuma") {
                val days = RecurrenceHelper.calculateDaysForNextRecurrence(item.recurrence, item.daysLeft)
                val nextItem = MaintenanceItem(
                    title = item.title,
                    category = item.category,
                    subtitle = item.subtitle ?: item.category,
                    daysLeft = days,
                    isCompleted = false,
                    notes = item.notes,
                    detailValue = item.detailValue,
                    smartReminder = item.smartReminder,
                    recurrence = item.recurrence,
                    alertDaysBefore = item.alertDaysBefore
                )
                val newId = dao.insertItem(nextItem)
                
                // Schedule notification for the new task
                NotificationScheduler.scheduleNotification(context, nextItem.copy(id = newId.toInt()))
            }
        }
    }

    suspend fun prepopulateIfEmpty(context: android.content.Context) {
        val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
        val hasPrepopulated = prefs.getBoolean("has_prepopulated", false)
        if (!hasPrepopulated) {
            val currentItemsList = allItems.first()
            if (currentItemsList.isEmpty()) {
                // Pre-populate maintenance items
                val defaultItems = listOf(
                    MaintenanceItem(
                        title = "Troca de óleo",
                        category = "CARRO",
                        subtitle = "CARRO",
                        daysLeft = -3,
                        isCompleted = false,
                        notes = "Venceu há 3 dias"
                    ),
                    MaintenanceItem(
                        title = "IPTU",
                        category = "CASA",
                        subtitle = "CASA",
                        daysLeft = -1,
                        isCompleted = false,
                        notes = "Venceu ontem"
                    ),
                    MaintenanceItem(
                        title = "Vencimento Seguro",
                        category = "CARRO",
                        subtitle = "CARRO",
                        daysLeft = 0,
                        isCompleted = false,
                        notes = "Vence hoje"
                    ),
                    MaintenanceItem(
                        title = "Revisão Ar Cond.",
                        category = "CASA",
                        subtitle = "Casa",
                        daysLeft = 4,
                        isCompleted = false,
                        notes = "Em 4 dias"
                    ),
                    MaintenanceItem(
                        title = "Limpeza Caixa",
                        category = "CASA",
                        subtitle = "Casa",
                        daysLeft = 6,
                        isCompleted = false,
                        notes = "Em 6 dias"
                    ),
                    MaintenanceItem(
                        title = "Dedetização",
                        category = "CASA",
                        subtitle = "Casa",
                        daysLeft = 28,
                        isCompleted = false,
                        notes = "Em 28 dias"
                    ),
                    MaintenanceItem(
                        title = "Troca de Filtro de Água",
                        category = "CASA",
                        subtitle = "Casa da Serra",
                        daysLeft = -3,
                        isCompleted = false,
                        notes = "Atrasado há 3 dias. Necessário trocar o refil da cozinha."
                    ),
                    MaintenanceItem(
                        title = "Cuidar do Jardim",
                        category = "CASA",
                        subtitle = "Casa da Serra",
                        daysLeft = 2,
                        isCompleted = false,
                        notes = "Adubação trimestral programada para esta semana."
                    ),
                    // Completed items for History tab
                    MaintenanceItem(
                        title = "Troca de Óleo e Filtro",
                        category = "CARRO",
                        subtitle = "Carro",
                        daysLeft = 30,
                        isCompleted = true,
                        completedDateStr = "12 Outubro 2023",
                        cost = 250.00,
                        notes = "Óleo sintético 5W30, serviço realizado na oficina central."
                    ),
                    MaintenanceItem(
                        title = "Limpeza das Calhas",
                        category = "CASA",
                        subtitle = "Casa",
                        daysLeft = 60,
                        isCompleted = true,
                        completedDateStr = "05 Setembro 2023",
                        cost = 0.0,
                        notes = "Remoção de folhas secas antes da temporada de chuvas intensas."
                    ),
                    MaintenanceItem(
                        title = "Manutenção Ar Condicionado",
                        category = "CASA",
                        subtitle = "Casa",
                        daysLeft = 80,
                        isCompleted = true,
                        completedDateStr = "15 Agosto 2023",
                        cost = 120.00,
                        notes = "Limpeza de filtros e verificação de gás refrigerante (Sala e Quarto)."
                    ),
                    MaintenanceItem(
                        title = "Poda das Árvores",
                        category = "CASA",
                        subtitle = "Jardim",
                        daysLeft = 100,
                        isCompleted = true,
                        completedDateStr = "10 Julho 2023",
                        cost = 0.0,
                        notes = "Poda realizada para controle de crescimento na frente."
                    )
                )

                for (item in defaultItems) {
                    dao.insertItem(item)
                }

                // Retrieve all inserted items and filter for completed ones to create history entries
                val insertedItems = dao.getAllItems().first()
                for (item in insertedItems) {
                    if (item.isCompleted) {
                        dao.insertHistoryEntry(
                            HistoryEntryEntity(
                                itemId = item.id,
                                title = item.title,
                                category = item.category,
                                subtitle = item.subtitle ?: item.category,
                                completedDateStr = item.completedDateStr ?: "Hoje",
                                cost = item.cost,
                                notes = item.notes
                            )
                        )
                    }
                }

                // Pre-populate app codes
                val defaultCodes = listOf(
                    AppCode(title = "Wi-Fi Casa", value = "Su3nh@F0rt3!", iconName = "wifi"),
                    AppCode(title = "Tinta Sala", value = "Suvinil #E2E8F0", iconName = "paint")
                )
                for (code in defaultCodes) {
                    dao.insertCode(code)
                }

                // Pre-populate quick notes
                dao.insertNote(
                    AppNote(
                        text = "Trocar filtro de água geladeira modelo XZ-200. Filtro tipo C.",
                        dateStr = "Hoje, 09:41"
                    )
                )

                // Pre-populate documents
                val defaultDocuments = listOf(
                    DocumentItem(fileName = "Manual_Geladeira_Brastemp.pdf", fileSize = "2.4 MB", fileType = "Eletrodomésticos"),
                    DocumentItem(fileName = "Garantia_Sofá_Sala.pdf", fileSize = "1.1 MB", fileType = "Recibos")
                )
                for (doc in defaultDocuments) {
                    dao.insertDocument(doc)
                }
            }
            prefs.edit().putBoolean("has_prepopulated", true).apply()
        }
    }
}
