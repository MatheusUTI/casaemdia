package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BentoArchiveScreen(
    viewModel: MainViewModel,
    onInicioClick: () -> Unit,
    onModulesClick: () -> Unit,
    onBackTimelineClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val codes by viewModel.codes.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val documents by viewModel.documents.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Filtered lists matching searchQuery
    val filteredCodes = codes.filter { it.title.contains(searchQuery, ignoreCase = true) || it.value.contains(searchQuery, ignoreCase = true) }
    val filteredNotes = notes.filter { it.text.contains(searchQuery, ignoreCase = true) }
    val filteredDocs = documents.filter { it.fileName.contains(searchQuery, ignoreCase = true) }

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
                onArchiveClick = {} // current active tab is archive
            )
        },
        containerColor = Background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Title Header with Quick Return Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Arquivo Vivo",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                    )
                    Text(
                        text = "Manuais, notas, senhas de Wi-Fi e fotos eletrônicas.",
                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                    )
                }

                IconButton(
                    onClick = onBackTimelineClick,
                    modifier = Modifier.background(SurfaceContainerLow, CircleShape)
                ) {
                    Icon(Icons.Default.Assignment, contentDescription = "Voltar Histórico Tarefas", tint = Primary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Beautiful Custom Sticky Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                placeholder = { Text("Buscar no arquivo...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Outline) },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = Outline)
                        }
                    } else {
                        Icon(Icons.Default.Mic, contentDescription = "Vocal", tint = Outline)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("archive_search_field"),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = OutlineVariant.copy(alpha = 0.5f),
                    focusedContainerColor = SurfaceContainerLowest,
                    unfocusedContainerColor = SurfaceContainerLowest
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bento Grid - Top Section containing Codes (Col 1) and Notes (Col 2)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Codes Box (Col 1)
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Pin, contentDescription = "Codes", tint = Primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Códigos",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        filteredCodes.forEach { code ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                                    .background(SurfaceContainerLow, RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (code.iconName == "wifi") Icons.Default.Wifi else Icons.Default.FormatPaint,
                                        contentDescription = code.iconName,
                                        tint = Outline,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(code.title, style = MaterialTheme.typography.labelSmall.copy(color = Outline))
                                        Text(code.value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                    }
                                }
                            }
                        }

                        TextButton(
                            onClick = {},
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("VER TODOS", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Primary, letterSpacing = 1.sp))
                        }
                    }
                }

                // Sticky Note Box (Col 2)
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0A0)), // Yellow sticky note layout
                    border = BorderStroke(1.dp, Color(0xFFF3C262))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EditNote, contentDescription = "Note", tint = Color(0xFF5F4100))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Notas",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF271900))
                            )
                        }

                        // Add note representation
                        Spacer(modifier = Modifier.height(12.dp))

                        if (filteredNotes.isNotEmpty()) {
                            val mainNote = filteredNotes.first()
                            Text(
                                text = mainNote.text,
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF271900), lineHeight = 18.sp),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 4
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = mainNote.dateStr,
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF5F4100))
                            )
                        } else {
                            Text("Nenhuma nota disponível.", style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF5F4100)))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // PDF Documents Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Folder, contentDescription = "Doc", tint = Secondary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Documentos",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                            )
                        }
                        Text(
                            text = "${documents.size} itens",
                            style = MaterialTheme.typography.bodySmall.copy(color = Outline)
                        )
                    }

                    HorizontalDivider(color = SurfaceContainer, thickness = 1.dp)

                    filteredDocs.forEach { doc ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {}
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFFFDAD6)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.PictureAsPdf, contentDescription = "PDF Icon", tint = Error)
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = doc.fileName,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Primary),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${doc.fileSize} • ${doc.fileType}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Outline)
                                    )
                                }
                            }
                            Icon(Icons.Default.MoreVert, contentDescription = "Ações", tint = Outline)
                        }
                        if (filteredDocs.indexOf(doc) < filteredDocs.size - 1) {
                            HorizontalDivider(color = SurfaceContainer.copy(alpha = 0.5f), thickness = 0.5.dp)
                        }
                    }

                    TextButton(
                        onClick = {},
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Text("ABRIR PASTA COMPLETA", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Primary, letterSpacing = 1.sp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Gallery Photos & Scans
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = "Scans", tint = PrimaryContainer)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Fotos & Scans",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                            )
                        }

                        IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = "Adicionar Foto", tint = Primary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Horizontal Thumbnails representation using beautiful drawing canvases
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThumbnailBlock(modifier = Modifier.weight(1f), label = "Recibo Pintura") {
                            // Draws a little paper receipt
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawRect(Color.White, topLeft = Offset(size.width * 0.2f, size.height * 0.1f), size = Size(size.width * 0.6f, size.height * 0.8f))
                                for (i in 0 until 5) {
                                    val y = size.height * 0.25f + i * 16f
                                    drawLine(Color.LightGray, start = Offset(size.width * 0.3f, y), end = Offset(size.width * 0.7f, y), strokeWidth = 2.dp.toPx())
                                }
                            }
                        }

                        ThumbnailBlock(modifier = Modifier.weight(1f), label = "Planta Baixa") {
                            // Draws a schematic room block representation
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawRect(Color(0xFFEFF6FF))
                                drawRect(
                                    color = Color(0xFF3B82F6),
                                    topLeft = Offset(size.width * 0.1f, size.height * 0.1f),
                                    size = Size(size.width * 0.8f, size.height * 0.8f),
                                    style = Stroke(width = 2.dp.toPx())
                                )
                                drawLine(Color(0xFF3B82F6), Offset(size.width * 0.5f, size.height * 0.1f), Offset(size.width * 0.5f, size.height * 0.9f))
                                drawLine(Color(0xFF3B82F6), Offset(size.width * 0.1f, size.height * 0.5f), Offset(size.width * 0.9f, size.height * 0.5f))
                            }
                        }

                        ThumbnailBlock(modifier = Modifier.weight(1f), label = "Quadro de Luz") {
                            // Draws an electrical box model
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawRect(Color(0xFFF3F4F6))
                                drawRect(
                                    color = Color.Gray,
                                    topLeft = Offset(size.width * 0.2f, size.height * 0.15f),
                                    size = Size(size.width * 0.6f, size.height * 0.7f),
                                    style = Stroke(width = 2.dp.toPx())
                                )
                                for (i in 0 until 4) {
                                    val x = size.width * 0.3f + i * 14f
                                    drawRect(Color.DarkGray, topLeft = Offset(x, size.height * 0.35f), size = Size(8f, 20f))
                                }
                            }
                        }

                        // Ver Mais Arrow Button
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceContainerLow)
                                .clickable {},
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.ArrowForward, contentDescription = "Ver Mais", tint = Outline)
                                Text("VER MAIS", style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp, color = Outline))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
