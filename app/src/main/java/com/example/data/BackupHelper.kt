package com.example.data

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Data class that holds all table records for full backup
data class BackupData(
    val maintenanceItems: List<MaintenanceItem>,
    val appCodes: List<AppCode>,
    val appNotes: List<AppNote>,
    val documentItems: List<DocumentItem>,
    val historyEntries: List<HistoryEntryEntity>
)

object BackupHelper {
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val adapterByClass = moshi.adapter(BackupData::class.java)

    /**
     * Serializes all repository data to a JSON String.
     */
    suspend fun exportToJsonString(repo: MaintenanceRepository): String {
        val items = repo.getAllItemsSync()
        val codes = repo.getAllCodesSync()
        val notes = repo.getAllNotesSync()
        val docs = repo.getAllDocumentsSync()
        val history = repo.getAllHistoryEntriesSync()

        val backup = BackupData(
            maintenanceItems = items,
            appCodes = codes,
            appNotes = notes,
            documentItems = docs,
            historyEntries = history
        )

        return adapterByClass.indent("  ").toJson(backup)
    }

    /**
     * Restores all data from a JSON String.
     * Clears the current database first, then refills it with the backup content.
     */
    suspend fun restoreFromJsonString(repo: MaintenanceRepository, json: String): Boolean {
        if (json.isBlank()) {
            throw IllegalArgumentException("O arquivo de backup está vazio.")
        }
        val backup = try {
            adapterByClass.fromJson(json)
        } catch (e: com.squareup.moshi.JsonDataException) {
            throw IllegalArgumentException("Os dados de backup possuem uma estrutura ou esquema incompatível.", e)
        } catch (e: java.io.IOException) {
            throw IllegalArgumentException("Erro ao ler o arquivo de backup físico.", e)
        } catch (e: Exception) {
            throw IllegalArgumentException("O arquivo de backup está corrompido ou possui formato JSON inválido.", e)
        } ?: throw IllegalArgumentException("Não foi possível decodificar os dados de backup.")

        try {
            repo.restoreDatabaseTransaction(
                items = backup.maintenanceItems,
                codes = backup.appCodes,
                notes = backup.appNotes,
                docs = backup.documentItems,
                history = backup.historyEntries
            )
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("Ocorreu um erro ao salvar os registros do backup no banco de dados local.", e)
        }
    }

    /**
     * Saves a JSON backup locally in the app's sandboxed directory.
     */
    suspend fun exportToLocalAppFolder(context: Context, repo: MaintenanceRepository): File? {
        return try {
            val json = exportToJsonString(repo)
            val backupDirectory = File(context.filesDir, "backups")
            if (!backupDirectory.exists()) {
                backupDirectory.mkdirs()
            }

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val backupFile = File(backupDirectory, "backup_${timestamp}.json")
            backupFile.writeText(json)
            backupFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Lists all sandboxed backups.
     */
    fun listLocalBackups(context: Context): List<File> {
        val backupDirectory = File(context.filesDir, "backups")
        if (!backupDirectory.exists()) return emptyList()
        return backupDirectory.listFiles { file -> file.extension == "json" }?.sortedByDescending { it.lastModified() }?.toList() ?: emptyList()
    }

    /**
     * Restores from a local sandboxed file.
     */
    suspend fun restoreFromLocalFile(repo: MaintenanceRepository, file: File): Boolean {
        val json = try {
            file.readText()
        } catch (e: Exception) {
            throw IllegalStateException("Não foi possível ler o arquivo de backup local.", e)
        }
        return restoreFromJsonString(repo, json)
    }

    /**
     * Writes database backup to an OutputStream (useful for SAF storage intents).
     */
    suspend fun writeBackupToStream(repo: MaintenanceRepository, outputStream: OutputStream): Boolean {
        return try {
            val json = exportToJsonString(repo)
            outputStream.use { stream ->
                stream.write(json.toByteArray(Charsets.UTF_8))
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Reads database backup from an InputStream (useful for SAF storage intents).
     */
    suspend fun restoreFromStream(repo: MaintenanceRepository, inputStream: InputStream): Boolean {
        val json = try {
            inputStream.use { stream ->
                stream.readBytes().toString(Charsets.UTF_8)
            }
        } catch (e: Exception) {
            throw IllegalStateException("Falha crítica ao ler o fluxo de dados de importação.", e)
        }
        return restoreFromJsonString(repo, json)
    }
}
