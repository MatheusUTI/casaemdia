package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    testTag: String,
    maxLength: Int,
    isError: Boolean,
    errorText: String?,
    charCounterTag: String,
    errorTextTag: String,
    optionalText: String,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    singleLine: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholderText) },
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag),
        shape = RoundedCornerShape(12.dp),
        isError = isError,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        supportingText = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (errorText != null) {
                    Text(
                        text = errorText,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.testTag(errorTextTag)
                    )
                } else {
                    Text(
                        text = optionalText,
                        color = OnSurfaceVariant.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    text = "${value.length}/$maxLength",
                    color = if (value.length > maxLength) MaterialTheme.colorScheme.error else OnSurfaceVariant.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.testTag(charCounterTag)
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primary,
            unfocusedBorderColor = OutlineVariant,
            errorBorderColor = MaterialTheme.colorScheme.error
        )
    )
}
