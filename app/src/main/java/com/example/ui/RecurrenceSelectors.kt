package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun RecurrenceSelector(
    selectedRecurrence: String,
    onRecurrenceSelected: (String) -> Unit
) {
    Column {
        Text(
            text = "RECORRÊNCIA",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val recOptions = listOf("Nenhuma", "Mensal", "Trimestral", "Semestral", "Anual")
            recOptions.forEach { opt ->
                val isSel = selectedRecurrence == opt
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onRecurrenceSelected(opt) }
                        .background(if (isSel) SecondaryContainer else SurfaceContainerLow, RoundedCornerShape(8.dp))
                        .border(
                            1.dp,
                            if (isSel) OnSecondaryContainer else OutlineVariant.copy(alpha = 0.4f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 10.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = opt,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSel) OnSecondaryContainer else Primary
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AlertDaysSelector(
    selectedDaysBefore: Int,
    onDaysBeforeSelected: (Int) -> Unit
) {
    Column {
        Text(
            text = "ALERTA ANTECIPADO",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val alertOptions = listOf(
                0 to "Hoje",
                1 to "1 dia",
                3 to "3 dias",
                7 to "1 sem",
                30 to "1 mês"
            )
            alertOptions.forEach { (days, optLabel) ->
                val isSel = selectedDaysBefore == days
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onDaysBeforeSelected(days) }
                        .background(if (isSel) SecondaryContainer else SurfaceContainerLow, RoundedCornerShape(8.dp))
                        .border(
                            1.dp,
                            if (isSel) OnSecondaryContainer else OutlineVariant.copy(alpha = 0.4f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 10.dp, horizontal = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = optLabel,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSel) OnSecondaryContainer else Primary
                        )
                    )
                }
            }
        }
    }
}
