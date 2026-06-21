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
    onSettingsClick: () -> Unit,
    onHistoryClick: (Int) -> Unit
) {
    val completedHistory by viewModel.historyEntries.collectAsState()
    var selectedFilter by remember { mutableStateOf("Todos") }
    var searchText by remember { mutableStateOf("") }
    var selectedSort by remember { mutableStateOf("Mais recentes") }
    var sortMenuExpanded by remember { mutableStateOf(false) }

    val filteredHistory = completedHistory.filter {
        val matchesCategory = when (selectedFilter) {
            "Casa" -> it.category == "CASA"
            "Carro" -> it.category == "CARRO"
            else -> true
        }
        val matchesSearch = it.title.contains(searchText, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    val sortedHistory = when (selectedSort) {
        "Mais antigos" -> filteredHistory.sortedBy { it.id }
        "Ordem alfabética" -> filteredHistory.sortedBy { it.title.lowercase() }
        else -> filteredHistory.sortedByDescending { it.id }
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

            Spacer(modifier = Modifier.height(12.dp))

            // Real-time title search box
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Pesquisar histórico...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Outline) },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = { searchText = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpar", tint = Outline)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("history_search_input"),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = OutlineVariant.copy(alpha = 0.5f),
                    focusedContainerColor = SurfaceContainerLowest,
                    unfocusedContainerColor = SurfaceContainerLowest
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category filter chips row with explicit options ("Todos", "Casa", "Carro")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("Todos", "Casa", "Carro").forEach { filter ->
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
                            .testTag("filter_chip_$filter")
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

            Spacer(modifier = Modifier.height(16.dp))

            // Total Completed counter and Sorting Selector Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("completed_total_counter_row"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Total completed counter
                Text(
                    text = "${completedHistory.size} concluídas no total",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    ),
                    modifier = Modifier.testTag("completed_total_counter")
                )

                // Sorting drop-down selector
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerLow)
                            .clickable { sortMenuExpanded = true }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("sort_selector")
                    ) {
                        Icon(Icons.Default.Sort, contentDescription = "Ordenação", tint = Primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = selectedSort,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                        )
                        Icon(
                            imageVector = if (sortMenuExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = "Expandir",
                            tint = Primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = sortMenuExpanded,
                        onDismissRequest = { sortMenuExpanded = false }
                    ) {
                        listOf("Mais recentes", "Mais antigos", "Ordem alfabética").forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedSort = option
                                    sortMenuExpanded = false
                                },
                                modifier = Modifier.testTag("sort_option_$option")
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Timeline container list
            if (sortedHistory.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    FriendlyEmptyState(
                        testTag = "history_empty_state",
                        title = "Histórico Limpo e Organizado",
                        description = "Aqui serão listados todos os seus históricos de tarefas executadas com custos e observações. No momento, nenhum registro foi localizado para os filtros ou busca informados.",
                        illustration = { HistoryEmptyIllustration() }
                    )
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
                        sortedHistory.forEach { item ->
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
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("history_item_card_${item.id}")
                                        .clickable { onHistoryClick(item.id) },
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
