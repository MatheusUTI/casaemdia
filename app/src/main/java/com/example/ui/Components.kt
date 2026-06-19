package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        // Toy Car
        val carX = w * 0.55f
        val carY = groundY + h * 0.02f
        drawRoundRect(
            color = Primary,
            topLeft = Offset(carX, carY),
            size = Size(w * 0.25f, h * 0.08f),
            cornerRadius = CornerRadius(10f, 10f)
        )
        drawRoundRect(
            color = OnPrimaryContainer,
            topLeft = Offset(carX + w * 0.05f, carY - h * 0.04f),
            size = Size(w * 0.15f, h * 0.05f),
            cornerRadius = CornerRadius(8f, 8f)
        )
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFECEB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(80.dp)
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
