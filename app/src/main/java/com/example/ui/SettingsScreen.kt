package com.example.ui

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.BackupHelper
import com.example.ui.theme.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var localBackups by remember { mutableStateOf(BackupHelper.listLocalBackups(context)) }

    // Navigation and Alerts
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var pendingImportFile by remember { mutableStateOf<File?>(null) }
    var pendingImportUri by remember { mutableStateOf<Uri?>(null) }

    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Storage pickers
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            try {
                val outputStream = context.contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    viewModel.exportToStream(outputStream, onSuccess = {
                        successMessage = "Backup exportado com sucesso para o dispositivo!"
                        Toast.makeText(context, "Backup exportado!", Toast.LENGTH_SHORT).show()
                    }, onError = {
                        errorMessage = "Ocorreu um erro ao exportar o arquivo."
                    })
                } else {
                    errorMessage = "Não foi possível abrir o fluxo de gravação."
                }
            } catch (e: Exception) {
                errorMessage = "Erro inesperado: ${e.localizedMessage}"
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            pendingImportUri = uri
            pendingImportFile = null
            showConfirmationDialog = true
        }
    }

    // Refresh function for sandbox files list
    val refreshLocalBackups = {
        localBackups = BackupHelper.listLocalBackups(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Configurações",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("btn_back_settings")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Success Message Card
            if (successMessage != null) {
                item {
                    AlertCard(
                        message = successMessage!!,
                        color = Color(0xFF15803D),
                        bgColor = Color(0xFFDCFCE7),
                        icon = Icons.Default.CheckCircle,
                        onDismiss = { successMessage = null }
                    )
                }
            }

            // Error Message Card
            if (errorMessage != null) {
                item {
                    AlertCard(
                        message = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        bgColor = MaterialTheme.colorScheme.errorContainer,
                        icon = Icons.Default.Error,
                        onDismiss = { errorMessage = null }
                    )
                }
            }

            // Section 1: Intro / Explanation
            item {
                BackupIntroCard()
            }

            // Section 2: External Backup (SAF - Standard Storage APIs)
            item {
                Text(
                    text = "BACKUP GLOBAL (ARQUIVO)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceVariant
                    ),
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            exportLauncher.launch("backup_casa_em_dia_${SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())}.json")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_export_backup_file")
                    ) {
                        Icon(Icons.Default.Upload, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Exportar...", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    }

                    Button(
                        onClick = {
                            importLauncher.launch(arrayOf("application/json"))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF002244)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_import_backup_file")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Importar...", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }

            // Section 3: Sandboxed Quick Backup (No permissions dialogs, quick & 100% stable)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "BACKUP RÁPIDO DO APLICATIVO",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceVariant
                        )
                    )

                    IconButton(
                        onClick = {
                            viewModel.backupToLocalAppFolder(
                                onSuccess = { file ->
                                    successMessage = "Cópia rápida criada com sucesso!"
                                    refreshLocalBackups()
                                },
                                onError = {
                                    errorMessage = "Falha ao criar o backup rápido do aplicativo."
                                }
                            )
                        },
                        modifier = Modifier.testTag("btn_create_quick_backup")
                    ) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Novo Backup Rápido", tint = Primary)
                    }
                }
            }

            if (localBackups.isEmpty()) {
                item {
                    FriendlyEmptyState(
                        testTag = "settings_empty_state",
                        title = "Nenhum Backup Rápido Encontrado",
                        description = "Crie uma cópia de segurança instantânea do seu banco de dados Room tocando no botão [+] no canto superior direito desta seção para blindar seus registros em sandbox rápida.",
                        illustration = { SettingsEmptyIllustration() }
                    )
                }
            } else {
                items(localBackups) { file ->
                    LocalBackupItemRow(
                        file = file,
                        onRestoreClick = {
                            pendingImportFile = file
                            pendingImportUri = null
                            showConfirmationDialog = true
                        },
                        onDeleteClick = {
                            if (file.delete()) {
                                successMessage = "Cópia rápida removida."
                                refreshLocalBackups()
                            } else {
                                errorMessage = "Não foi possível remover a cópia rápida."
                            }
                        }
                    )
                }
            }
        }
    }

    // Confirmation Alert Dialog before completely overwriting local database
    if (showConfirmationDialog) {
        ConfirmationRestoreDialog(
            onDismissRequest = { showConfirmationDialog = false },
            onConfirm = {
                showConfirmationDialog = false
                if (pendingImportFile != null) {
                    viewModel.restoreFromLocalFile(
                        file = pendingImportFile!!,
                        onSuccess = {
                            successMessage = "Backup restaurado com sucesso! Seus dados foram sincronizados."
                            pendingImportFile = null
                        },
                        onError = { errorMsg ->
                            errorMessage = errorMsg
                            pendingImportFile = null
                        }
                    )
                } else if (pendingImportUri != null) {
                    try {
                        val inputStream = context.contentResolver.openInputStream(pendingImportUri!!)
                        if (inputStream != null) {
                            viewModel.restoreFromStream(
                                inputStream = inputStream,
                                onSuccess = {
                                    successMessage = "Backup importado e restaurado com absoluto sucesso!"
                                    pendingImportUri = null
                                },
                                onError = { errorMsg ->
                                    errorMessage = errorMsg
                                    pendingImportUri = null
                                }
                            )
                        } else {
                            errorMessage = "Não foi possível carregar os dados do arquivo."
                            pendingImportUri = null
                        }
                    } catch (e: Exception) {
                        errorMessage = "Erro na abertura do fluxo de leitura: ${e.localizedMessage}"
                        pendingImportUri = null
                    }
                }
            }
        )
    }
}
