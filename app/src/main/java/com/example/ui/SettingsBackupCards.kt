package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import androidx.compose.ui.platform.testTag

@Composable
fun BackupIntroCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Backup,
                contentDescription = "Backup",
                tint = Primary,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    "Segurança dos Seus Dados",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Selecione as opções abaixo para salvar backups completos de suas rotinas de manutenção, códigos de utilidade e anotações.",
                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                )
            }
        }
    }
}

@Composable
fun AlertCard(
    message: String,
    color: Color,
    bgColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onDismiss() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                color = color,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Fechar alerta",
                tint = color.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun LocalBackupItemRow(
    file: File,
    onRestoreClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val dateText = remember(file) {
        try {
            val nameParts = file.nameWithoutExtension.split("_")
            if (nameParts.size >= 3) {
                val dateStr = nameParts[1]
                val timeStr = nameParts[2]
                val year = dateStr.substring(0, 4)
                val month = dateStr.substring(4, 6)
                val day = dateStr.substring(6, 8)
                val hour = timeStr.substring(0, 2)
                val min = timeStr.substring(2, 4)
                "$day/$month/$year às $hour:$min"
            } else {
                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(file.lastModified()))
            }
        } catch (e: Exception) {
            "Cópia Local"
        }
    }

    val fileSizeText = remember(file) {
        val bytes = file.length()
        if (bytes < 1024) "$bytes B"
        else "${String.format(Locale.getDefault(), "%.1f", bytes / 1024.0)} KB"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.FileCopy, contentDescription = null, tint = Primary)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Primary),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = fileSizeText,
                        style = MaterialTheme.typography.bodySmall.copy(color = Outline)
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = onRestoreClick,
                    modifier = Modifier
                        .size(36.dp)
                        .background(PrimaryContainer, CircleShape)
                        .testTag("btn_restore_selected_quick_backup")
                ) {
                    Icon(
                        imageVector = Icons.Default.Restore,
                        contentDescription = "Restaurar este backup",
                        tint = OnPrimaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.errorContainer, CircleShape)
                        .testTag("btn_delete_selected_quick_backup")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir este backup",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
