package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppCode
import com.example.data.AppNote
import com.example.data.DocumentItem
import com.example.data.MaintenanceItem
import com.example.ui.theme.*

// --- CUSTOM SVG DRAWINGS VIA CANVAS FOR HIGHER FIDELITY ---

@Composable
fun WaterDropIcon(modifier: Modifier = Modifier, color: Color = Secondary) {
    Canvas(modifier = modifier.size(24.dp)) {
        val width = size.width
        val height = size.height
        val path = Path().apply {
            moveTo(width / 2f, height * 0.15f)
            cubicTo(
                width * 0.15f, height * 0.60f,
                width * 0.10f, height * 0.90f,
                width / 2f, height * 0.95f
            )
            cubicTo(
                width * 0.90f, height * 0.90f,
                width * 0.85f, height * 0.60f,
                width / 2f, height * 0.15f
            )
            close()
        }
        drawPath(path = path, color = color)
    }
}

@Composable
fun SnowflakeIcon(modifier: Modifier = Modifier, color: Color = Primary) {
    Canvas(modifier = modifier.size(24.dp)) {
        val width = size.width
        val height = size.height
        val center = Offset(width / 2, height / 2)
        val armLength = width * 0.35f

        // Draw 6 arms
        for (i in 0 until 6) {
            val angle = (i * 60) * (Math.PI / 180).toFloat()
            val endX = center.x + armLength * kotlin.math.cos(angle).toFloat()
            val endY = center.y + armLength * kotlin.math.sin(angle).toFloat()
            drawLine(
                color = color,
                start = center,
                end = Offset(endX, endY),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Draw V branch for each arm
            val branchAngle1 = angle + (30 * Math.PI / 180).toFloat()
            val branchAngle2 = angle - (30 * Math.PI / 180).toFloat()
            val branchLen = armLength * 0.4f
            val midX = center.x + (armLength * 0.6f) * kotlin.math.cos(angle).toFloat()
            val midY = center.y + (armLength * 0.6f) * kotlin.math.sin(angle).toFloat()

            drawLine(
                color = color,
                start = Offset(midX, midY),
                end = Offset(
                    midX + branchLen * kotlin.math.cos(branchAngle1).toFloat(),
                    midY + branchLen * kotlin.math.sin(branchAngle1).toFloat()
                ),
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = color,
                start = Offset(midX, midY),
                end = Offset(
                    midX + branchLen * kotlin.math.cos(branchAngle2).toFloat(),
                    midY + branchLen * kotlin.math.sin(branchAngle2).toFloat()
                ),
                strokeWidth = 2.5.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun BugIcon(modifier: Modifier = Modifier, color: Color = Outline) {
    Canvas(modifier = modifier.size(24.dp)) {
        val width = size.width
        val height = size.height
        val cx = width / 2f
        val cy = height / 2f

        // Body
        drawOval(
            color = color,
            topLeft = Offset(cx - width * 0.2f, cy - height * 0.3f),
            size = Size(width * 0.4f, height * 0.6f)
        )

        // Head
        drawCircle(
            color = color,
            radius = width * 0.15f,
            center = Offset(cx, cy - height * 0.35f)
        )

        // Legs (3 pairs)
        val legsY = listOf(cy - height * 0.15f, cy, cy + height * 0.15f)
        for (y in legsY) {
            drawLine(
                color = color,
                start = Offset(cx - width * 0.15f, y),
                end = Offset(cx - width * 0.45f, y - height * 0.05f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = color,
                start = Offset(cx + width * 0.15f, y),
                end = Offset(cx + width * 0.45f, y - height * 0.05f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Antennae
        drawLine(
            color = color,
            start = Offset(cx - width * 0.05f, cy - height * 0.45f),
            end = Offset(cx - width * 0.25f, cy - height * 0.58f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(cx + width * 0.05f, cy - height * 0.45f),
            end = Offset(cx + width * 0.25f, cy - height * 0.58f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun StylizedHouseIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Ground pedestal (isometric ellipse)
        val groundY = h * 0.72f
        drawOval(
            color = Color(0xFFDCEAFE),
            topLeft = Offset(w * 0.1f, groundY - h * 0.1f),
            size = Size(w * 0.8f, h * 0.25f)
        )

        // Pedestal layers
        drawOval(
            color = Color(0xFFC0DBFF),
            topLeft = Offset(w * 0.1f, groundY - h * 0.08f),
            size = Size(w * 0.8f, h * 0.23f)
        )

        // House building block (3D styled prism)
        // Draw main faces using customized paths
        val roofPeak = Offset(w * 0.5f, h * 0.25f)
        val roofLeft = Offset(w * 0.28f, h * 0.45f)
        val roofRight = Offset(w * 0.72f, h * 0.45f)
        val frontBottomLeft = Offset(w * 0.32f, groundY - h * 0.05f)
        val frontBottomRight = Offset(w * 0.68f, groundY - h * 0.05f)

        // Left Face (Front-Left in isometric projection)
        val leftFacePath = Path().apply {
            moveTo(roofPeak.x, roofPeak.y)
            lineTo(roofLeft.x, roofLeft.y)
            lineTo(frontBottomLeft.x, frontBottomLeft.y)
            lineTo(w * 0.5f, groundY)
            close()
        }
        drawPath(leftFacePath, Color(0xFF93C5FD))

        // Right Face
        val rightFacePath = Path().apply {
            moveTo(roofPeak.x, roofPeak.y)
            lineTo(roofRight.x, roofRight.y)
            lineTo(frontBottomRight.x, frontBottomRight.y)
            lineTo(w * 0.5f, groundY)
            close()
        }
        drawPath(rightFacePath, Color(0xFF3B82F6))

        // Roof Left
        val roofLeftPath = Path().apply {
            moveTo(roofPeak.x, roofPeak.y)
            lineTo(roofLeft.x, roofLeft.y)
            lineTo(w * 0.25f, h * 0.42f)
            lineTo(w * 0.5f, h * 0.20f)
            close()
        }
        drawPath(roofLeftPath, Color(0xFFDBEAFE))

        // Roof Right
        val roofRightPath = Path().apply {
            moveTo(roofPeak.x, roofPeak.y)
            lineTo(roofRight.x, roofRight.y)
            lineTo(w * 0.75f, h * 0.42f)
            lineTo(w * 0.5f, h * 0.20f)
            close()
        }
        drawPath(roofRightPath, Color(0xFF1D4ED8))

        // Windows (2D/3D blocks)
        drawRect(
            color = Color.White,
            topLeft = Offset(w * 0.4f, h * 0.52f),
            size = Size(w * 0.07f, h * 0.08f)
        )
        drawRect(
            color = Color.White,
            topLeft = Offset(w * 0.53f, h * 0.52f),
            size = Size(w * 0.07f, h * 0.08f)
        )

        // Toy Car (Isometric blocks)
        val carX = w * 0.55f
        val carY = groundY + h * 0.02f
        // Car Body
        drawRoundRect(
            color = Primary,
            topLeft = Offset(carX, carY),
            size = Size(w * 0.25f, h * 0.08f),
            cornerRadius = CornerRadius(10f, 10f)
        )
        // Car Cabin
        drawRoundRect(
            color = OnPrimaryContainer,
            topLeft = Offset(carX + w * 0.05f, carY - h * 0.04f),
            size = Size(w * 0.15f, h * 0.05f),
            cornerRadius = CornerRadius(8f, 8f)
        )
        // Wheels
        drawCircle(
            color = Color.DarkGray,
            radius = 12f,
            center = Offset(carX + w * 0.06f, carY + h * 0.08f)
        )
        drawCircle(
            color = Color.DarkGray,
            radius = 12f,
            center = Offset(carX + w * 0.19f, carY + h * 0.08f)
        )
    }
}


// --- 1. SPECIALIZED ONBOARDING SCREEN ---

@Composable
fun OnboardingScreen(
    onStartClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("onboarding_screen"),
        containerColor = Background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Title
            Text(
                text = "Casa em Dia",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                    letterSpacing = (-0.5).sp
                ),
                modifier = Modifier.padding(top = 24.dp)
            )

            // Central Illustration Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 40.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(SurfaceContainerLowest)
                    .border(1.dp, OutlineVariant.copy(alpha = 0.3f), RoundedCornerShape(32.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                // Beautiful interactive vector drawing replacing the failed prompt
                StylizedHouseIllustration(modifier = Modifier.fillMaxSize())
            }

            // Descriptive Information Block
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Tudo sob controle,\nnada esquecido.",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Primary,
                        textAlign = TextAlign.Center,
                        lineHeight = 36.sp,
                        letterSpacing = (-0.5).sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Gerencie prazos, manutenções e documentos da sua casa e carro em um só lugar.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = OnSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Action Button
            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("onboarding_start_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF002244)),
                shape = RoundedCornerShape(28.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "COMEÇAR AGORA",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Começar",
                        tint = Color.White
                    )
                }
            }
        }
    }
}


// --- 2. MAIN DASHBOARD SCREEN (INÍCIO) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onModulesClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onVehicleDetailClick: () -> Unit,
    onHouseDetailClick: () -> Unit
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
                    overdue.indexOf(item)
                    OverdueTaskCard(
                        item = item,
                        onClick = {
                            if (item.category == "CARRO") onVehicleDetailClick() else onHouseDetailClick()
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
                            if (item.category == "CARRO") onVehicleDetailClick() else onHouseDetailClick()
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
                                    if (item.category == "CARRO") onVehicleDetailClick() else onHouseDetailClick()
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
                            if (item.category == "CARRO") onVehicleDetailClick() else onHouseDetailClick()
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}


// --- 3. MODULES SCREEN (MÓDULOS) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModulesScreen(
    onInicioClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onVehicleClick: () -> Unit,
    onHouseClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
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
                currentTab = "modulos",
                onInicioClick = onInicioClick,
                onModulesClick = {},
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
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Seus Módulos",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            )

            Text(
                text = "Gerencie suas responsabilidades de forma organizada e eficiente.",
                style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant),
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Module Card - Carro
            ModuleSelectionCard(
                title = "Carro",
                description = "Manutenções, IPVA, seguro e histórico do seu veículo.",
                icon = Icons.Default.DirectionsCar,
                iconBg = PrimaryContainer,
                iconColor = OnPrimaryContainer,
                onClick = onVehicleClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Module Card - Casa
            ModuleSelectionCard(
                title = "Casa",
                description = "Rotinas de limpeza, reformas, pagamentos recorrentes e controle de garantias.",
                icon = Icons.Default.Home,
                iconBg = Color(0xFFDCFCE7),
                iconColor = Color(0xFF15803D),
                onClick = onHouseClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Module Card - Arquivo Vivo
            ModuleSelectionCard(
                title = "Arquivo Vivo",
                description = "Documentos importantes, manuais, notas fiscais e registros médicos.",
                icon = Icons.Default.Inventory,
                iconBg = Color(0xFFFEF3C7),
                iconColor = Color(0xFFB45309),
                onClick = onArchiveClick
            )
        }
    }
}


// --- 4. VEHICLE DETAIL SCREEN ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailScreen(
    onBackClick: () -> Unit,
    onFullHistoryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalhe do Veículo",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Primary)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Primary)
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
            // Hero Vehicle Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6))
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                // Background Car Outline representation
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = Path().apply {
                        moveTo(size.width * 0.15f, size.height * 0.7f)
                        lineTo(size.width * 0.3f, size.height * 0.65f)
                        lineTo(size.width * 0.45f, size.height * 0.38f)
                        lineTo(size.width * 0.72f, size.height * 0.38f)
                        lineTo(size.width * 0.85f, size.height * 0.65f)
                        lineTo(size.width * 0.95f, size.height * 0.75f)
                        close()
                    }
                    drawPath(path, Color.White.copy(alpha = 0.12f))
                }

                Column {
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "USO DIÁRIO",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Palio 1.6",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold, color = Color.White)
                    )

                    Text(
                        text = "BRA2E19  •  Ano 2022",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.9f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Próxima Manutenção Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Próxima Manutenção",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                    )
                    Text(
                        text = "Agendamento recomendado em breve",
                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF3C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Build, contentDescription = "Maintenance Required", tint = Color(0xFFD97706), modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Inner Recommended Action Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerLow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DirectionsCar, contentDescription = "Car", tint = Primary, modifier = Modifier.size(18.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Troca de Óleo e Filtros",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                            )
                            Text(
                                text = "Revisão programada para 46.000 km",
                                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFEF2F2), RoundedCornerShape(8.dp))
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "RESTAM APENAS",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Error)
                            )
                            Text(
                                "500 km",
                                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Current Odometer Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainerLow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Speed, contentDescription = "Odometer", tint = Primary, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "ODÔMETRO ATUAL",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
                    )

                    Text(
                        text = "45.500 km",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .background(SurfaceContainerLow, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "ATUALIZADO HÁ 2 DIAS",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, color = Outline)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Principal Documents Section
            Text(
                text = "Documentos Principais",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Documents List
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
            ) {
                Column {
                    DetailDocumentRow(
                        title = "Seguro Auto",
                        statusStr = "Ativo até Dez/2024",
                        isOk = true
                    )
                    HorizontalDivider(color = SurfaceContainer, thickness = 1.dp)
                    DetailDocumentRow(
                        title = "CRLV 2023",
                        statusStr = "Regularizado",
                        isOk = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

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


// --- 5. HOUSE DETAIL SCREEN ---

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


// --- 6. NEW ITEM DIALOG / SCREEN (NOVO ITEM) ---

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


// --- 7. HISTÓRICO DE MANUTENÇÃO (TIMELINE & ARQUIVO PART I) ---

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
                                                style = MaterialTheme.typography.bodyMedium.copy(color = OnSurface, lineHeight = 18.sp)
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


// --- 8. ARQUIVO VIVO BENTO EXPLORER SCREEN ---

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
                                drawStrokeRect(Color(0xFF3B82F6), Offset(size.width * 0.1f, size.height * 0.1f), Size(size.width * 0.8f, size.height * 0.8f))
                                drawLine(Color(0xFF3B82F6), Offset(size.width * 0.5f, size.height * 0.1f), Offset(size.width * 0.5f, size.height * 0.9f))
                                drawLine(Color(0xFF3B82F6), Offset(size.width * 0.1f, size.height * 0.5f), Offset(size.width * 0.9f, size.height * 0.5f))
                            }
                        }

                        ThumbnailBlock(modifier = Modifier.weight(1f), label = "Quadro de Luz") {
                            // Draws an electrical box model
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawRect(Color(0xFFF3F4F6))
                                drawStrokeRect(Color.Gray, Offset(size.width * 0.2f, size.height * 0.15f), Size(size.width * 0.6f, size.height * 0.7f))
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


// --- REUSABLE CUSTOM UI COMPONENT BLOCKS ---

@Composable
fun AppBottomNav(
    currentTab: String,
    onInicioClick: () -> Unit,
    onModulesClick: () -> Unit,
    onArchiveClick: () -> Unit
) {
    NavigationBar(
        containerColor = SurfaceContainerLowest,
        tonalElevation = 8.dp,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(68.dp)
    ) {
        NavigationBarItem(
            selected = currentTab == "inicio",
            onClick = onInicioClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
            label = { Text("Início", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Primary,
                selectedTextColor = Primary,
                unselectedIconColor = Outline,
                unselectedTextColor = Outline,
                indicatorColor = SecondaryContainer
            ),
            modifier = Modifier.testTag("nav_tab_inicio")
        )

        NavigationBarItem(
            selected = currentTab == "modulos",
            onClick = onModulesClick,
            icon = { Icon(Icons.Default.GridView, contentDescription = "Módulos") },
            label = { Text("Módulos", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Primary,
                selectedTextColor = Primary,
                unselectedIconColor = Outline,
                unselectedTextColor = Outline,
                indicatorColor = SecondaryContainer
            ),
            modifier = Modifier.testTag("nav_tab_modulos")
        )

        NavigationBarItem(
            selected = currentTab == "arquivo",
            onClick = onArchiveClick,
            icon = { Icon(Icons.Default.Inventory, contentDescription = "Arquivo") },
            label = { Text("Arquivo", fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Primary,
                selectedTextColor = Primary,
                unselectedIconColor = Outline,
                unselectedTextColor = Outline,
                indicatorColor = SecondaryContainer
            ),
            modifier = Modifier.testTag("nav_tab_arquivo")
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    icon: ImageVector,
    iconColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
        )
    }
}

@Composable
fun OverdueTaskCard(
    item: MaintenanceItem,
    onClick: () -> Unit,
    onResolve: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFECEB)), // light reddish background
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // Red bar decoration on left edge
        Row {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(Error)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        if (item.category == "CARRO") {
                            Icon(Icons.Default.DirectionsCar, contentDescription = "Car", tint = Error)
                        } else {
                            Icon(Icons.Default.Home, contentDescription = "Home", tint = Error)
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = item.subtitle ?: item.category,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Error)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.notes ?: "Venceu há tempo",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onResolve,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Marcar completo", tint = Secondary)
                }
            }
        }
    }
}

@Composable
fun StandardTaskCard(
    item: MaintenanceItem,
    accentColor: Color,
    tagColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(accentColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.category == "CARRO") {
                        Icon(Icons.Default.Security, contentDescription = "Seguro", tint = tagColor)
                    } else {
                        BugIcon(color = tagColor)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(accentColor, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = item.subtitle ?: item.category,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = tagColor)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item.notes ?: "Vence hoje",
                            style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                        )
                    }
                }
            }

            Icon(Icons.Default.ChevronRight, contentDescription = "Ver detalhe", tint = Outline)
        }
    }
}

@Composable
fun GridTaskCard(
    item: MaintenanceItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (item.title.contains("Cond")) Color(0xFFDCFCE7) else Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.title.contains("Cond")) {
                        SnowflakeIcon(color = Color(0xFF15803D))
                    } else {
                        WaterDropIcon(color = Color(0xFF2563EB))
                    }
                }
            }

            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Primary),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.subtitle ?: "Casa",
                        style = MaterialTheme.typography.bodySmall.copy(color = Outline)
                    )
                    Text(
                        text = item.notes ?: "4 dias",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF15803D), fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun TaskEmptyCard(msg: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow.copy(alpha = 0.5f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = msg,
                style = MaterialTheme.typography.bodyMedium.copy(color = Outline, textAlign = TextAlign.Center)
            )
        }
    }
}

@Composable
fun ModuleSelectionCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Icon(Icons.Default.ChevronRight, contentDescription = "Acessar", tint = Outline)
        }
    }
}

@Composable
fun CategorySelectChip(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) SecondaryContainer else SurfaceContainerLow)
            .border(
                1.5.dp,
                if (isSelected) OnSecondaryContainer else OutlineVariant.copy(alpha = 0.4f),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) OnSecondaryContainer else Primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) OnSecondaryContainer else Primary
                )
            )
        }
    }
}

@Composable
fun HouseModuleButton(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier.height(96.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {}
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerLow),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Primary),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DetailDocumentRow(
    title: String,
    statusStr: String,
    isOk: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {}
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Ok",
                    tint = Secondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Primary)
                )
                Text(
                    text = statusStr,
                    style = MaterialTheme.typography.bodySmall.copy(color = Secondary, fontWeight = FontWeight.SemiBold)
                )
            }
        }

        Icon(Icons.Default.ChevronRight, contentDescription = "Acessar", tint = Outline)
    }
}

@Composable
fun ThumbnailBlock(
    modifier: Modifier = Modifier,
    label: String,
    drawCanvas: @Composable BoxScope.() -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceContainerLow)
                .border(0.5.dp, OutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
        ) {
            drawCanvas()
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Outline, fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// Draw a boundary box in Canvas
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStrokeRect(
    color: Color,
    offset: Offset,
    size: Size,
    strokeWidth: Float = 3f
) {
    drawRect(
        color = color,
        topLeft = offset,
        size = size,
        style = Stroke(width = strokeWidth)
    )
}
