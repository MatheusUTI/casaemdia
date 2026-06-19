package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewItemScreen(
    viewModel: MainViewModel,
    onCloseClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("CASA") } // default CASA
    var notes by remember { mutableStateOf("") }
    var smartReminder by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Novo Item",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCloseClick) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Topic input field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("O que precisa ser feito?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("new_item_title_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = OutlineVariant
                    ),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(24.dp))

                // CATEGORIA Title
                Text(
                    text = "CATEGORIA",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Selector tabs row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategorySelectChip(
                        modifier = Modifier.weight(1f),
                        label = "Carro",
                        isSelected = category == "CARRO",
                        icon = Icons.Default.DirectionsCar,
                        onClick = { category = "CARRO" }
                    )

                    CategorySelectChip(
                        modifier = Modifier.weight(1f),
                        label = "Casa",
                        isSelected = category == "CASA",
                        icon = Icons.Default.Home,
                        onClick = { category = "CASA" }
                    )

                    CategorySelectChip(
                        modifier = Modifier.weight(1f),
                        label = "Outro",
                        isSelected = category == "OUTRO",
                        icon = Icons.Default.MoreHoriz,
                        onClick = { category = "OUTRO" }
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Notes / Description field
                Text(
                    text = "DESCRIÇÃO OU OBSERVAÇÃO",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("Digite notas adicionais, marcas, prazos ou códigos...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = OutlineVariant
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Picker simulator
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Date", tint = Primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Selecionar data",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                                )
                                Text(
                                    "Opcional (hoje por padrão)",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Outline)
                                )
                            }
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = "Selecionar", tint = Outline)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Intelligent reminder
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = "Reminder", tint = Color(0xFFD97706))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Lembrete Inteligente",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                                )
                                Text(
                                    "Aviso 2 dias antes por notificação",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Outline)
                                )
                            }
                        }
                        Switch(
                            checked = smartReminder,
                            onCheckedChange = { smartReminder = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Primary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Save Button
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.addMaintenanceItem(
                            title = title,
                            category = category,
                            subtitle = if (category == "CARRO") "CARRO" else "CASA",
                            daysLeft = (0..20).random(), // puts it under standard today/7day or 30day lists
                            notes = notes,
                            smartReminder = smartReminder
                        )
                        onSaveSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("save_item_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF002244)),
                shape = RoundedCornerShape(28.dp),
                enabled = title.isNotBlank()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Check", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Salvar Item",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }
            }
        }
    }
}
