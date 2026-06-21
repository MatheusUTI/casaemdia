package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onModulesClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onVehicleDetailClick: () -> Unit,
    onHouseDetailClick: () -> Unit,
    onItemClick: (Int) -> Unit
) {
    val items by viewModel.items.collectAsState()

    // Filters and groupings
    val overdue = items.filter { !it.isCompleted && it.daysLeft < 0 }
    val today = items.filter { !it.isCompleted && it.daysLeft == 0 }
    val next7Days = items.filter { !it.isCompleted && it.daysLeft in 1..7 }
    val next30Days = items.filter { !it.isCompleted && it.daysLeft in 8..30 }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Casa em Dia",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
                    // Profile Headshot representation
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PrimaryContainer)
                            .border(1.5.dp, SecondaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AM",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configurações",
                            tint = Primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFF002244),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .testTag("fab_add_item")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Item",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        bottomBar = {
            AppBottomNav(
                currentTab = "inicio",
                onInicioClick = {},
                onModulesClick = onModulesClick,
                onArchiveClick = onArchiveClick
            )
        },
        containerColor = Background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Overdue Section ("Atrasados" - Red)
            if (overdue.isNotEmpty()) {
                SectionHeader(title = "Atrasados", icon = Icons.Default.Warning, iconColor = Error)
                overdue.forEach { item ->
                    OverdueTaskCard(
                        item = item,
                        onClick = {
                            onItemClick(item.id)
                        },
                        onResolve = { viewModel.completeItem(item.id) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Today Section ("Hoje" - Yellow/Orange)
            SectionHeader(title = "Hoje", icon = Icons.Default.CalendarToday, iconColor = Color(0xFFD97706))
            if (today.isEmpty()) {
                TaskEmptyCard("Nenhum compromisso para hoje!")
            } else {
                today.forEach { item ->
                    StandardTaskCard(
                        item = item,
                        accentColor = Color(0xFFFEF3C7),
                        tagColor = Color(0xFFD97706),
                        onClick = {
                            onItemClick(item.id)
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Next 7 Days Section ("Próximos 7 dias" - Green)
            SectionHeader(title = "Próximos 7 dias", icon = Icons.Default.DateRange, iconColor = Secondary)
            if (next7Days.isEmpty()) {
                TaskEmptyCard("Nenhuma tarefa para os próximos 7 dias.")
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    next7Days.forEach { item ->
                        Box(modifier = Modifier.weight(1f)) {
                            GridTaskCard(
                                item = item,
                                onClick = {
                                    onItemClick(item.id)
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Next 30 Days Section ("Próximos 30 dias" - Slate)
            SectionHeader(title = "Próximos 30 dias", icon = Icons.Default.EventNote, iconColor = Outline)
            if (next30Days.isEmpty()) {
                TaskEmptyCard("Tudo limpo para os próximos 30 dias!")
            } else {
                next30Days.forEach { item ->
                    StandardTaskCard(
                        item = item,
                        accentColor = SurfaceContainerLow,
                        tagColor = Primary,
                        onClick = {
                            onItemClick(item.id)
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
