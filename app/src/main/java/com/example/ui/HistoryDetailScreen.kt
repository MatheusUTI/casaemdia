package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import com.example.data.RecurrenceHelper
import com.example.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(
    viewModel: MainViewModel,
    historyId: Int,
    onBackClick: () -> Unit,
    onRestoreSuccess: () -> Unit,
    onDeleteSuccess: () -> Unit
) {
    val historyEntries by viewModel.historyEntries.collectAsState()
    val entry = remember(historyEntries, historyId) { historyEntries.find { it.id == historyId } }
    
    val items by viewModel.items.collectAsState()
    val originalItem = remember(items, entry) { entry?.let { ent -> items.find { it.id == ent.itemId } } }
    
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Excluir do Histórico") },
            text = { Text("Deseja realmente remover esta entrada do histórico permanentemente? Isso não afetará itens ativos.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val title = entry?.title ?: "Item"
                        viewModel.deleteHistoryEntry(historyId)
                        Toast.makeText(context, "Registro de '$title' excluído!", Toast.LENGTH_SHORT).show()
                        showDeleteConfirm = false
                        onDeleteSuccess()
                    },
                    modifier = Modifier.testTag("confirm_delete_history_button")
                ) {
                    Text("Excluir", color = Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalhes do Histórico",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Primary)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.testTag("delete_history_button")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Excluir do Histórico", tint = Error)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { innerPadding ->
        if (entry == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Category Badge and "Concluído" Badge Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category Badge
                        val catIcon = when (entry.category) {
                            "CARRO" -> Icons.Default.DirectionsCar
                            "CASA" -> Icons.Default.Home
                            else -> Icons.Default.Label
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PrimaryContainer,
                            contentColor = Color.White
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(catIcon, contentDescription = null, modifier = Modifier.size(16.dp))
                                Text(
                                    text = when (entry.category) {
                                        "CARRO" -> "Carro"
                                        "CASA" -> "Casa"
                                        else -> entry.category
                                    },
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        // Status Concluído Badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFD1FAE5),
                            contentColor = Color(0xFF065F46)
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                text = "Concluído",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Title
                    Text(
                        text = entry.title,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Primary),
                        modifier = Modifier.testTag("history_detail_title")
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Dates Card (Original and Completed)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Completed Date row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Data de Conclusão", tint = Secondary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "Data de Conclusão",
                                        style = MaterialTheme.typography.labelMedium.copy(color = Outline)
                                    )
                                    Text(
                                        text = entry.completedDateStr ?: "Hoje",
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Primary),
                                        modifier = Modifier.testTag("history_detail_completed_date")
                                    )
                                }
                            }

                            // Original date if available
                            val originalDateStr = if (originalItem != null) {
                                val limitDate = LocalDate.now().plusDays(originalItem.daysLeft.toLong())
                                val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", java.util.Locale("pt", "BR"))
                                try {
                                    limitDate.format(formatter)
                                } catch (e: Exception) {
                                    null
                                }
                            } else {
                                null
                            }

                            if (originalDateStr != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(color = OutlineVariant.copy(alpha = 0.4f))
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = "Data Original", tint = Primary, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(
                                            text = "Data Original de Prazo",
                                            style = MaterialTheme.typography.labelMedium.copy(color = Outline)
                                        )
                                        Text(
                                            text = originalDateStr,
                                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Primary),
                                            modifier = Modifier.testTag("history_detail_original_date")
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Configurações do Lembrete Card
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("history_config_card"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Residência Vinculada (Vínculo)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val linkIcon = if (entry.category == "CARRO") Icons.Default.DirectionsCar else Icons.Default.Home
                                val linkLabel = if (entry.category == "CARRO") "Veículo Vinculado" else "Residência Vinculada"
                                val linkVal = if (entry.category == "CARRO") "Garagem / Carro Principal" else "Casa / Residência Principal"
                                Icon(linkIcon, contentDescription = linkLabel, tint = Primary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(text = linkLabel, style = MaterialTheme.typography.labelMedium.copy(color = Outline))
                                    Text(text = linkVal, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Primary))
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = OutlineVariant.copy(alpha = 0.3f))
                            Spacer(modifier = Modifier.height(12.dp))

                            // Recorrência
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Autorenew, contentDescription = "Recorrência", tint = Primary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(text = "Recorrência", style = MaterialTheme.typography.labelMedium.copy(color = Outline))
                                    val recurrenceVal = originalItem?.let { RecurrenceHelper.getRecurrenceLabel(it.recurrence) } ?: "Nenhuma / Arquivado"
                                    Text(text = recurrenceVal, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Primary))
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = OutlineVariant.copy(alpha = 0.3f))
                            Spacer(modifier = Modifier.height(12.dp))

                            // Alerta Antecipado
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.NotificationsActive, contentDescription = "Alerta Antecipado", tint = Primary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(text = "Alerta Antecipado", style = MaterialTheme.typography.labelMedium.copy(color = Outline))
                                    val alertVal = originalItem?.let { RecurrenceHelper.getAlertLabel(it.alertDaysBefore) } ?: "Sem alerta de antecedência"
                                    Text(text = alertVal, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Primary))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Description text / notes card
                    Text(
                        text = "DESCRIÇÃO OU OBSERVAÇÃO",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = if (entry.notes.isNullOrBlank()) "Sem observações adicionais." else entry.notes,
                                style = MaterialTheme.typography.bodyMedium.copy(color = OnSurface),
                                modifier = Modifier.testTag("history_detail_notes")
                            )
                        }
                    }
                }

                // Restore Button at bottom
                Column {
                    Spacer(modifier = Modifier.height(40.dp))
                    Button(
                        onClick = {
                            viewModel.restoreHistoryEntry(historyId)
                            Toast.makeText(context, "Lembrete '${entry.title}' restaurado para lista ativa!", Toast.LENGTH_SHORT).show()
                            onRestoreSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("restore_history_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Restore, contentDescription = "Restaurar", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Restaurar Lembrete",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                            )
                        }
                    }
                }
            }
        }
    }
}
