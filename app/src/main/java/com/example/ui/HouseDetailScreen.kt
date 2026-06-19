package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseDetailScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onFullHistoryClick: () -> Unit
) {
    val items by viewModel.items.collectAsState()
    val attentionItems = items.filter { !it.isCompleted && it.category == "CASA" && it.title in listOf("Troca de Filtro de Água", "Cuidar do Jardim") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalhe da Residência",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Primary)
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
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // House Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF0F766E), Color(0xFF14B8A6))
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                // Stylized house drawing outline background
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val roofY = size.height * 0.45f
                    val path = Path().apply {
                        moveTo(size.width * 0.4f, size.height * 0.8f)
                        lineTo(size.width * 0.4f, roofY)
                        lineTo(size.width * 0.6f, roofY * 0.6f)
                        lineTo(size.width * 0.8f, roofY)
                        lineTo(size.width * 0.8f, size.height * 0.8f)
                        close()
                    }
                    drawPath(path, Color.White.copy(alpha = 0.08f))
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Residência", tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "RESIDÊNCIA PRINCIPAL",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.8f))
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Casa da Serra",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submodules Grid ("Gestão da Casa")
            Text(
                text = "Gestão da Casa",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HouseModuleButton(
                    modifier = Modifier.weight(1f),
                    title = "Manutenção",
                    icon = Icons.Default.Build,
                    color = Color(0xFF0F766E)
                )

                HouseModuleButton(
                    modifier = Modifier.weight(1f),
                    title = "Documentos",
                    icon = Icons.Default.Description,
                    color = Color(0xFF0F766E)
                )

                HouseModuleButton(
                    modifier = Modifier.weight(1f),
                    title = "Contas Fixas",
                    icon = Icons.Default.Receipt,
                    color = Color(0xFF0F766E)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Attention Required Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Atenção Necessária",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                )

                Box(
                    modifier = Modifier
                        .background(Color(0xFFFEE2E2), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${attentionItems.size} Tarefas",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFFB91C1C))
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Attention list items
            if (attentionItems.isEmpty()) {
                TaskEmptyCard("Nenhuma pendência na Casa da Serra!")
            } else {
                attentionItems.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.Top) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(if (item.daysLeft < 0) Color(0xFFFFEAEA) else Color(0xFFFFFAEA)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (item.title.contains("Filtro")) {
                                        WaterDropIcon(color = if (item.daysLeft < 0) Error else Color(0xFFD97706))
                                    } else {
                                        Icon(Icons.Default.Yard, contentDescription = "Jardim", tint = Color(0xFF15803D))
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                                    )
                                    Text(
                                        text = item.notes ?: "",
                                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                if (item.title.contains("Filtro")) {
                                    TextButton(
                                        onClick = { viewModel.completeItem(item.id) },
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Text("RESOLVER", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = Error))
                                    }
                                } else {
                                    TextButton(
                                        onClick = { viewModel.completeItem(item.id) },
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Text("AGENDAR", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFFD97706)))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Full History Check Button
            OutlinedButton(
                onClick = onFullHistoryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                border = BorderStroke(1.5.dp, Primary)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.History, contentDescription = "Historic", tint = Primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ver Histórico Completo",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = Primary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
