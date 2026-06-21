package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun FriendlyEmptyState(
    modifier: Modifier = Modifier,
    testTag: String = "friendly_empty_state",
    title: String,
    description: String,
    illustration: @Composable () -> Unit,
    action: (@Composable () -> Unit)? = null
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
            animationSpec = tween(600),
            initialOffsetY = { 50 }
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp, horizontal = 16.dp)
                .testTag(testTag),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(SurfaceContainerLowest),
                contentAlignment = Alignment.Center
            ) {
                illustration()
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                    letterSpacing = (-0.3).sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = OnSurfaceVariant,
                    lineHeight = 22.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (action != null) {
                Spacer(modifier = Modifier.height(24.dp))
                action()
            }
        }
    }
}

@Composable
fun HomeEmptyIllustration(modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier.size(120.dp)) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        // Soft green glowing background ring
        drawCircle(
            color = Color(0xFFDCFCE7),
            radius = w * 0.45f,
            center = Offset(cx, cy)
        )
        // Solid green shield indicator
        drawCircle(
            color = Color(0xFF22C55E),
            radius = w * 0.32f,
            center = Offset(cx, cy)
        )
        // White check symbol inside the shield
        val checkPath = Path().apply {
            moveTo(cx - w * 0.12f, cy)
            lineTo(cx - w * 0.02f, cy + w * 0.10f)
            lineTo(cx + w * 0.15f, cy - w * 0.08f)
        }
        drawPath(
            path = checkPath,
            color = Color.White,
            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Sparkle bubbles
        drawCircle(
            color = Color(0xFF4ADE80),
            radius = 6.dp.toPx(),
            center = Offset(cx - w * 0.35f, cy - h * 0.25f)
        )
        drawCircle(
            color = Color(0xFF4ADE80),
            radius = 4.dp.toPx(),
            center = Offset(cx + w * 0.35f, cy + h * 0.20f)
        )
        drawCircle(
            color = Color(0xFF4ADE80),
            radius = 5.dp.toPx(),
            center = Offset(cx + w * 0.25f, cy - h * 0.30f)
        )
    }
}

@Composable
fun HistoryEmptyIllustration(modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier.size(120.dp)) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        // Soft primary background ring
        drawCircle(
            color = Color(0xFFE0F2FE),
            radius = w * 0.45f,
            center = Offset(cx, cy)
        )

        // Draw a document folder outline or timeline node
        val folderPath = Path().apply {
            moveTo(cx - w * 0.25f, cy - h * 0.20f)
            lineTo(cx - w * 0.05f, cy - h * 0.20f)
            lineTo(cx + w * 0.05f, cy - h * 0.10f)
            lineTo(cx + w * 0.25f, cy - h * 0.10f)
            lineTo(cx + w * 0.25f, cy + h * 0.25f)
            lineTo(cx - w * 0.25f, cy + h * 0.25f)
            close()
        }
        drawPath(
            path = folderPath,
            color = Color(0xFF0284C7)
        )

        // Draw clock hands over it to represent history
        drawCircle(
            color = Color.White,
            radius = w * 0.15f,
            center = Offset(cx, cy + h * 0.05f)
        )
        drawLine(
            color = Color(0xFF0284C7),
            start = Offset(cx, cy + h * 0.05f),
            end = Offset(cx, cy - h * 0.03f),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color(0xFF0284C7),
            start = Offset(cx, cy + h * 0.05f),
            end = Offset(cx + w * 0.07f, cy + h * 0.05f),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun SettingsEmptyIllustration(modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier.size(120.dp)) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        // Light amber/gray background ring
        drawCircle(
            color = Color(0xFFFEF3C7),
            radius = w * 0.45f,
            center = Offset(cx, cy)
        )

        // Draw database cylinder style illustration
        drawOval(
            color = Color(0xFFD97706),
            topLeft = Offset(cx - w * 0.20f, cy - h * 0.20f),
            size = Size(w * 0.40f, h * 0.12f)
        )

        val path = Path().apply {
            moveTo(cx - w * 0.20f, cy - h * 0.14f)
            lineTo(cx - w * 0.20f, cy + h * 0.15f)
            cubicTo(
                cx - w * 0.15f, cy + h * 0.21f,
                cx + w * 0.15f, cy + h * 0.21f,
                cx + w * 0.20f, cy + h * 0.15f
            )
            lineTo(cx + w * 0.20f, cy - h * 0.14f)
            close()
        }
        drawPath(path = path, color = Color(0xFFF59E0B))

        drawOval(
            color = Color(0xFFD97706),
            topLeft = Offset(cx - w * 0.20f, cy - h * 0.04f),
            size = Size(w * 0.40f, h * 0.12f),
            style = Stroke(width = 2.dp.toPx())
        )

        drawCircle(
            color = Color.White,
            radius = 6.dp.toPx(),
            center = Offset(cx, cy + h * 0.08f)
        )
    }
}
