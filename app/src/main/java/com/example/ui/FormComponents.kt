package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun CategorySelectorSection(
    category: String,
    onCategorySelected: (String) -> Unit
) {
    Column {
        Text(
            text = "CATEGORIA",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CategorySelectChip(
                modifier = Modifier.weight(1f),
                label = "Carro",
                isSelected = category == "CARRO",
                icon = Icons.Default.DirectionsCar,
                onClick = { onCategorySelected("CARRO") }
            )

            CategorySelectChip(
                modifier = Modifier.weight(1f),
                label = "Casa",
                isSelected = category == "CASA",
                icon = Icons.Default.Home,
                onClick = { onCategorySelected("CASA") }
            )

            CategorySelectChip(
                modifier = Modifier.weight(1f),
                label = "Outro",
                isSelected = category == "OUTRO",
                icon = Icons.Default.MoreHoriz,
                onClick = { onCategorySelected("OUTRO") }
            )
        }
    }
}

@Composable
fun DatePickerCard(
    dateText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("select_date_card"),
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
                        dateText,
                        style = MaterialTheme.typography.bodySmall.copy(color = Outline)
                    )
                }
            }
            Icon(Icons.Default.ChevronRight, contentDescription = "Selecionar", tint = Outline)
        }
    }
}

@Composable
fun IntelligentReminderSection(
    smartReminder: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
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
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Primary
                )
            )
        }
    }
}
