package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveTimelineScreen(
    viewModel: MainViewModel,
    onInicioClick: () -> Unit,
    onModulesClick: () -> Unit,
    onSearchClick: () -> Unit, // leads to bento search file explorer
    onSettingsClick: () -> Unit
) {
    val items by viewModel.items.collectAsState()
    var selectedFilter by remember { mutableStateOf("Todos") }

    val completedItems = items.filter { it.isCompleted }
    val filteredHistory = when (selectedFilter) {
        "Casa" -> completedItems.filter { it.category == "CASA" }
        "Carro" -> completedItems.filter { it.category == "CARRO" }
        "Jardim" -> completedItems.filter { it.subtitle?.lowercase()?.contains("jardim") == true }
        else -> completedItems
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Casa em Dia",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                        )
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("AM", color = Color.White, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Configurações", tint = Primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        bottomBar = {
            AppBottomNav(
                currentTab = "arquivo",
                onInicioClick = onInicioClick,
                onModulesClick = onModulesClick,
                onArchiveClick = {} // current
            )
        },
        containerColor = Background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Screen Title & Switch Tab
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Histórico de Manutenção",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Primary,
                            letterSpacing = (-0.5).sp
                        )
                    )
                    Text(
                        text = "Acompanhe as tarefas concluídas e custos.",
                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                    )
                }

                // Bento search button switch icon
                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier
                        .background(SurfaceContainerLow, CircleShape)
                        .testTag("switch_to_bento_button")
                ) {
                    Icon(Icons.Default.Folder, contentDescription = "Pasta Arquivos Documentais", tint = Primary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filter chips horizontal scroll row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("Todos", "Casa", "Carro", "Jardim").forEach { filter ->
                    val isSelected = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) PrimaryContainer else SurfaceContainerLow)
                            .border(
                                1.dp,
                                if (isSelected) Primary else OutlineVariant.copy(alpha = 0.4f),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = filter,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Primary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Timeline container list
            if (filteredHistory.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.History, contentDescription = "Histórico Vazio", modifier = Modifier.size(48.dp), tint = Outline)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Histórico está limpo!", style = MaterialTheme.typography.bodyLarge.copy(color = Outline))
                    }
                }
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    // Vertical continuous timeline line
                    Box(
                        modifier = Modifier
                            .offset(x = 19.dp)
                            .fillMaxHeight()
                            .width(2.dp)
                            .background(SurfaceContainerHigh)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        filteredHistory.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                // Left Green Checked Symbol representing Dot in Timeline
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .border(2.dp, Secondary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Concluída",
                                        tint = Secondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                // Timeline card
                                Card(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                                    border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                        .clip(CircleShape)
                                                        .background(SurfaceContainerLow),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    if (item.category == "CARRO") {
                                                        Icon(Icons.Default.DirectionsCar, "Carro", tint = Primary, modifier = Modifier.size(16.dp))
                                                    } else {
                                                        Icon(Icons.Default.Home, "Casa", tint = Primary, modifier = Modifier.size(16.dp))
                                                    }
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = item.title,
                                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = item.completedDateStr ?: "Entregue",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, color = Outline)
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        // Cost box if it exists
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(SurfaceContainerLow, RoundedCornerShape(8.dp))
                                                .padding(10.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Payments,
                                                contentDescription = "Custo",
                                                tint = Outline,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (item.cost != null && item.cost > 0) {
                                                    "R$ ${"%.2f".format(item.cost)} - ${item.notes}"
                                                } else {
                                                    item.notes ?: "Nenhuma observação informada."
                                                },
                                                style = MaterialTheme.typography.bodyMedium.copy(color = OnSurface, lineHeight = 30.sp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
