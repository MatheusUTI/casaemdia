package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*
import java.time.LocalDate
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewItemScreen(
    viewModel: MainViewModel,
    itemId: Int? = null,
    onCloseClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val items by viewModel.items.collectAsState()
    val itemToEdit = remember(items, itemId) { items.find { it.id == itemId } }
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("CASA") } // default CASA
    var notes by remember { mutableStateOf("") }
    var smartReminder by remember { mutableStateOf(true) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var recurrence by remember { mutableStateOf("Nenhuma") }
    var alertDaysBefore by remember { mutableStateOf(0) }

    var titleTouched by remember { mutableStateOf(false) }

    val titleMaxChars = 50
    val notesMaxChars = 200

    val isTitleTooLong = title.length > titleMaxChars
    val isTitleError = (titleTouched && title.isBlank()) || isTitleTooLong
    val titleErrorText = when {
        isTitleTooLong -> "Título não pode exceder $titleMaxChars caracteres"
        titleTouched && title.isBlank() -> "O título é obrigatório"
        else -> null
    }

    val isNotesTooLong = notes.length > notesMaxChars
    val isNotesError = isNotesTooLong
    val notesErrorText = if (isNotesTooLong) "Descrição não pode exceder $notesMaxChars caracteres" else null

    val isSaveEnabled = title.isNotBlank() && !isTitleTooLong && !isNotesTooLong

    LaunchedEffect(itemToEdit) {
        if (itemToEdit != null) {
            title = itemToEdit.title
            category = itemToEdit.category
            notes = itemToEdit.notes ?: ""
            smartReminder = itemToEdit.smartReminder
            selectedDate = LocalDate.now().plusDays(itemToEdit.daysLeft.toLong())
            recurrence = itemToEdit.recurrence
            alertDaysBefore = itemToEdit.alertDaysBefore
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            selectedDate = Instant.ofEpochMilli(selectedMillis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (itemId != null) "Editar Item" else "Novo Item",
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
                ValidatedTextField(
                    value = title,
                    onValueChange = { 
                        title = it
                        titleTouched = true
                    },
                    placeholderText = "O que precisa ser feito?",
                    testTag = "new_item_title_field",
                    maxLength = titleMaxChars,
                    isError = isTitleError,
                    errorText = titleErrorText,
                    charCounterTag = "title_char_counter",
                    errorTextTag = "title_error_text",
                    optionalText = "Obrigatório",
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(24.dp))

                CategorySelectorSection(
                    category = category,
                    onCategorySelected = { category = it }
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Notes / Description field
                Text(
                    text = "DESCRIÇÃO OU OBSERVAÇÃO",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Notes / Description field
                ValidatedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholderText = "Digite notas adicionais, marcas, prazos ou códigos...",
                    testTag = "new_item_notes_field",
                    maxLength = notesMaxChars,
                    isError = isNotesError,
                    errorText = notesErrorText,
                    charCounterTag = "notes_char_counter",
                    errorTextTag = "notes_error_text",
                    optionalText = "Opcional",
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Picker simulator
                val dateText = if (selectedDate != null) {
                    selectedDate!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                } else {
                    "Opcional (hoje por padrão)"
                }
                DatePickerCard(
                    dateText = dateText,
                    onClick = { showDatePicker = true }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Recorrência
                RecurrenceSelector(
                    selectedRecurrence = recurrence,
                    onRecurrenceSelected = { recurrence = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Alerta Antecipado
                AlertDaysSelector(
                    selectedDaysBefore = alertDaysBefore,
                    onDaysBeforeSelected = { alertDaysBefore = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                IntelligentReminderSection(
                    smartReminder = smartReminder,
                    onCheckedChange = { smartReminder = it }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Save Button
            Button(
                onClick = {
                    if (isSaveEnabled) {
                        val days = if (selectedDate != null) {
                            ChronoUnit.DAYS.between(LocalDate.now(), selectedDate).toInt()
                        } else {
                            0
                        }
                        if (itemId != null) {
                            viewModel.updateMaintenanceItem(
                                id = itemId,
                                title = title,
                                category = category,
                                subtitle = if (category == "CARRO") "CARRO" else "CASA",
                                daysLeft = days,
                                notes = notes,
                                smartReminder = smartReminder,
                                recurrence = recurrence,
                                alertDaysBefore = alertDaysBefore
                            )
                            Toast.makeText(context, "Lembrete editado com sucesso!", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.addMaintenanceItem(
                                title = title,
                                category = category,
                                subtitle = if (category == "CARRO") "CARRO" else "CASA",
                                daysLeft = days,
                                notes = notes,
                                smartReminder = smartReminder,
                                recurrence = recurrence,
                                alertDaysBefore = alertDaysBefore
                            )
                            Toast.makeText(context, "Lembrete salvo com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                        onSaveSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("save_item_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF002244),
                    disabledContainerColor = Color(0xFF002244).copy(alpha = 0.4f),
                    disabledContentColor = Color.White.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(28.dp),
                enabled = isSaveEnabled
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Check", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (itemId != null) "Atualizar Item" else "Salvar Item",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }
            }
        }
    }
}
