package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun KpiDashboard(
    activeCount: Int,
    completedCount: Int,
    overdueCount: Int,
    todayCount: Int,
    next7DaysCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .testTag("kpi_dashboard_container")
    ) {
        Text(
            text = "Estatísticas de Manutenção",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Primary
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Row 1: Primary Metrics (Ativos & Concluídos)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                KpiCard(
                    title = "Ativos",
                    value = activeCount.toString(),
                    icon = Icons.Default.Assignment,
                    iconColor = Primary,
                    containerColor = SurfaceContainerLowest,
                    borderColor = OutlineVariant.copy(alpha = 0.5f),
                    tagSuffix = "ativos"
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                KpiCard(
                    title = "Concluídos",
                    value = completedCount.toString(),
                    icon = Icons.Default.CheckCircle,
                    iconColor = Secondary,
                    containerColor = SecondaryContainer.copy(alpha = 0.3f),
                    borderColor = Secondary.copy(alpha = 0.4f),
                    tagSuffix = "concluidos"
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Row 2: Secondary / Urgent Metrics (Atrasados, Hoje, Próximos 7 Dias)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                KpiCard(
                    title = "Atrasados",
                    value = overdueCount.toString(),
                    icon = Icons.Default.Warning,
                    iconColor = Error,
                    containerColor = ErrorContainer.copy(alpha = 0.3f),
                    borderColor = Error.copy(alpha = 0.3f),
                    tagSuffix = "atrasados",
                    valueColor = Error
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                KpiCard(
                    title = "Hoje",
                    value = todayCount.toString(),
                    icon = Icons.Default.Today,
                    iconColor = Color(0xFFD97706),
                    containerColor = Color(0xFFFEF3C7).copy(alpha = 0.5f),
                    borderColor = Color(0xFFFCD34D),
                    tagSuffix = "hoje",
                    valueColor = Color(0xFFB45309)
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                KpiCard(
                    title = "7 dias",
                    value = next7DaysCount.toString(),
                    icon = Icons.Default.DateRange,
                    iconColor = Primary,
                    containerColor = SurfaceContainerLow,
                    borderColor = OutlineVariant.copy(alpha = 0.3f),
                    tagSuffix = "proximos_7"
                )
            }
        }
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    containerColor: Color,
    borderColor: Color,
    tagSuffix: String,
    valueColor: Color = Primary
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .testTag("kpi_card_$tagSuffix"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            // Icon top right
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor.copy(alpha = 0.8f),
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.TopEnd)
            )

            // Content Left
            Column(
                modifier = Modifier.align(Alignment.BottomStart),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = valueColor,
                        lineHeight = 28.sp
                    ),
                    modifier = Modifier.testTag("kpi_value_$tagSuffix")
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = OnSurfaceVariant.copy(alpha = 0.8f)
                    ),
                    maxLines = 1
                )
            }
        }
    }
}
