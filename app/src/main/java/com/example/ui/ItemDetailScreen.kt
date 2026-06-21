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
import com.example.data.MaintenanceItem
import com.example.data.RecurrenceHelper
import com.example.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    viewModel: MainViewModel,
    itemId: Int,
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onResolveSuccess: () -> Unit
) {
    val items by viewModel.items.collectAsState()
    val item = remember(items, itemId) { items.find { it.id == itemId } }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Excluir Lembrete") },
            text = { Text("Deseja realmente excluir este lembrete permanentemente?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val title = item?.title ?: "Lembrete"
                        viewModel.deleteMaintenanceItem(itemId)
                        Toast.makeText(context, "Lembrete '$title' excluído com sucesso!", Toast.LENGTH_SHORT).show()
                        showDeleteConfirm = false
                        onResolveSuccess()
                    },
                    modifier = Modifier.testTag("confirm_delete_button")
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
                        text = "Detalhes do Lembrete",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Primary)
                    }
                },
                actions = {
                    if (item != null && !item.isCompleted) {
                        IconButton(
                            onClick = { onEditClick(item.id) },
                            modifier = Modifier.testTag("edit_item_button")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Primary)
                        }
                        IconButton(
                            onClick = { showDeleteConfirm = true },
                            modifier = Modifier.testTag("delete_item_button")
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Error)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { innerPadding ->
        if (item == null) {
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
                    // Category Badge and Status Badge Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category Badge
                        val catIcon = when (item.category) {
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
                                    text = when (item.category) {
                                        "CARRO" -> "Carro"
                                        "CASA" -> "Casa"
                                        else -> "Lembrete"
                                    },
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        // Status Badge
                        val (statusText, statusBg, statusColor) = when {
                            item.isCompleted -> Triple("Concluído", Color(0xFFD1FAE5), Color(0xFF065F46))
                            item.daysLeft < 0 -> Triple("Atrasado", Color(0xFFFEE2E2), Color(0xFF991B1B))
                            item.daysLeft == 0 -> Triple("Hoje", Color(0xFFFEF3C7), Color(0xFF92400E))
                            item.daysLeft in 1..7 -> Triple("Atenção", Color(0xFFFFF3E0), Color(0xFFE65100))
                            else -> Triple("OK", Color(0xFFEFF6FF), Color(0xFF1E40AF))
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = statusBg,
                            contentColor = statusColor
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                text = statusText,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Title / Description
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Primary),
                        modifier = Modifier.testTag("item_detail_title")
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Data/Prazo Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Data do compromisso", tint = Primary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                val limitDate = LocalDate.now().plusDays(item.daysLeft.toLong())
                                val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", java.util.Locale("pt", "BR"))
                                val dateStr = try {
                                    limitDate.format(formatter)
                                } catch (e: Exception) {
                                    limitDate.toString()
                                }

                                Text(
                                    text = "Data de Prazo",
                                    style = MaterialTheme.typography.labelMedium.copy(color = Outline)
                                )
                                Text(
                                    text = dateStr,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Primary),
                                    modifier = Modifier.testTag("item_detail_date")
                                )
                                if (!item.isCompleted) {
                                    val daysLeftText = when {
                                        item.daysLeft < 0 -> "${-item.daysLeft} dias atrasado"
                                        item.daysLeft == 0 -> "Hoje"
                                        item.daysLeft == 1 -> "Amanhã"
                                        else -> "${item.daysLeft} dias restantes"
                                    }
                                    Text(
                                        text = daysLeftText,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = if (item.daysLeft <= 0) Error else Secondary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Configurações do Lembrete Card
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("item_config_card"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Residência Vinculada (Vínculo)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val linkIcon = if (item.category == "CARRO") Icons.Default.DirectionsCar else Icons.Default.Home
                                val linkLabel = if (item.category == "CARRO") "Veículo Vinculado" else "Residência Vinculada"
                                val linkVal = if (item.category == "CARRO") "Garagem / Carro Principal" else "Casa / Residência Principal"
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
                                    Text(text = RecurrenceHelper.getRecurrenceLabel(item.recurrence), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Primary))
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
                                    Text(text = RecurrenceHelper.getAlertLabel(item.alertDaysBefore), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Primary))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Description text
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
                                text = if (item.notes.isNullOrBlank()) "Sem observações adicionais." else item.notes,
                                style = MaterialTheme.typography.bodyMedium.copy(color = OnSurface),
                                modifier = Modifier.testTag("item_detail_description")
                            )
                        }
                    }
                }

                // Complete / Resolve Button at bottom
                if (!item.isCompleted) {
                    Spacer(modifier = Modifier.height(40.dp))
                    Button(
                        onClick = {
                            viewModel.completeItem(item.id)
                            Toast.makeText(context, "Lembrete '${item.title}' concluído e arquivado!", Toast.LENGTH_SHORT).show()
                            onResolveSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("mark_as_done_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A6C44)), // Green
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = "Concluir", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Marcar como Feito",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                            )
                        }
                    }
                }
            }
        }
    }
}
